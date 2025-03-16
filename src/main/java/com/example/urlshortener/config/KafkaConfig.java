package com.example.urlshortener.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic urlShortenerTopic() {
        return new NewTopic("url-shortener-topic", 1, (short) 1);
    }
}
