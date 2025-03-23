package com.example.shortenit.service;

import io.micrometer.core.annotation.Timed;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.shortenit.model.UrlMapping;
import com.example.shortenit.repository.UrlRepository;

@Service
public class UrlShorteningService implements IUrlShorteningService {
    private final UrlRepository urlRepository;
    private final KafkaProducerService kafkaProducerService;
    private final RedisService redisService;

    public UrlShorteningService(
        UrlRepository urlRepository,
        KafkaProducerService kafkaProducerService,
        RedisService redisService
    ) {
        this.urlRepository = urlRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.redisService = redisService;
    }
    @Timed(value = "shortenit.shorten.url", description = "Time taken to shorten URLs")
    public String shortenUrl(String longUrl) {
        String shortUrl = generateShortUrl(longUrl);
    
        redisService.cache(shortUrl, longUrl);
    
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setLongUrl(longUrl);
        urlRepository.save(urlMapping);
    
        kafkaProducerService.sendUrlShortenedMessage(shortUrl, longUrl);
    
        return shortUrl;
    }
    @Timed(value = "shortenit.expand.url", description = "Time taken to resolve short URLs")
    public Optional<String> getLongUrl(String shortUrl) {
        Optional<String> cachedUrl = redisService.get(shortUrl);
        if (cachedUrl.isPresent()) {
            return cachedUrl;
        }
    
        Optional<String> longUrl = urlRepository.findById(shortUrl)
                .map(UrlMapping::getLongUrl);
    
        longUrl.ifPresent(url -> redisService.cache(shortUrl, url));
    
        return longUrl;
    }

    private String generateShortUrl(String longUrl) {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // private static final String BASE62_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // private String encodeBase62(byte[] hash) {
    //     StringBuilder encoded = new StringBuilder();
    //     for (byte b : hash) {
    //         encoded.append(BASE62_ALPHABET.charAt((b & 0xFF) % 62));
    //     }
    //     return encoded.toString();
    // }

}
