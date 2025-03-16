package com.example.urlshortener.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    
    @KafkaListener(topics = "url-shortener-topic", groupId = "url-shortener-group")
    public void listenForUrlShortened(String message) {
        System.out.println("Consumed message: " + message);
    }
}   
