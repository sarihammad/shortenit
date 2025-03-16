package com.example.urlshortener.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;

@Service
public class UrlShorteningService {
    private final UrlRepository urlRepository;
    private final KafkaProducerService kafkaProducerService;
    private final StringRedisTemplate redisTemplate;

    public UrlShorteningService(UrlRepository urlRepository, KafkaProducerService kafkaProducerService, StringRedisTemplate redisTemplate) {
        this.urlRepository = urlRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.redisTemplate = redisTemplate;
    }

    public String shortenUrl(String longUrl) {
        String shortUrl = generateShortUrl(longUrl);

        // Cache in Redis
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(shortUrl, longUrl, 7, TimeUnit.DAYS); // for 7 days
        
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setLongUrl(longUrl);
        urlRepository.save(urlMapping);

        kafkaProducerService.sendUrlShortenedMessage(shortUrl, longUrl);

        return shortUrl;
    }

    public Optional<String> getLongUrl(String shortUrl) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String cachedUrl = ops.get(shortUrl);
        if (cachedUrl != null) {
            return Optional.of(cachedUrl);
        }

        Optional<String> longUrl = urlRepository.findById(shortUrl).map(UrlMapping::getLongUrl);

        longUrl.ifPresent(url -> ops.set(shortUrl, url, 7, TimeUnit.DAYS));

        return longUrl;
    }

    private String generateShortUrl(String longUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(longUrl.getBytes(StandardCharsets.UTF_8));
            return encodeBase62(hash).substring(0, 6);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating short URL", e);
        }
    }

    private static final String BASE62_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String encodeBase62(byte[] hash) {
        StringBuilder encoded = new StringBuilder();
        for (byte b : hash) {
            encoded.append(BASE62_ALPHABET.charAt((b & 0xFF) % 62));
        }
        return encoded.toString();
    }

}
