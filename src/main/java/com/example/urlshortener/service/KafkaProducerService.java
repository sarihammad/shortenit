package com.example.urlshortener.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUrlShortenedMessage(String shortUrl, String longUrl) {
        String message = "Shortened URL: " + shortUrl + " -> " + longUrl;
        kafkaTemplate.send("url-shortener-topic", message);
        System.out.println("Produced message: " + message);
    }
}
