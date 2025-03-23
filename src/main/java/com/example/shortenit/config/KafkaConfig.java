package com.example.shortenit.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic urlShortenerTopic() {
        return new NewTopic("shortenit-topic", 1, (short) 1);
    }
}
