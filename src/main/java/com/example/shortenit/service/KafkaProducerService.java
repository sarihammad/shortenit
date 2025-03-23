package com.example.shortenit.service;

import com.example.shortenit.kafka.ShortenedUrlEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUrlShortenedMessage(String shortUrl, String longUrl) {
        ShortenedUrlEvent event = new ShortenedUrlEvent(shortUrl, longUrl);
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("shortenit-topic", payload);
            log.info("Produced message to Kafka: {}", payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize ShortenedUrlEvent", e);
        }
    }
}