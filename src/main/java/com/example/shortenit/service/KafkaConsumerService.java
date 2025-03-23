package com.example.shortenit.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.shortenit.kafka.ShortenedUrlEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerService {
    
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "shortenit-topic", groupId = "shortenit-group")
    public void listenForUrlShortened(String message) throws JsonProcessingException {
        ShortenedUrlEvent event = new ObjectMapper().readValue(message, ShortenedUrlEvent.class);
        log.info("Consumed shortened URL: {} -> {}", event.getShortUrl(), event.getLongUrl());
    }
}
