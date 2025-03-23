package com.example.shortenit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.shortenit.ShortenItApplication;
import com.example.shortenit.config.PostConstruct;
import com.example.shortenit.model.UrlMapping;
import com.example.shortenit.service.IUrlShorteningService;

@SpringBootTest(
webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,    
classes = {
    ShortenItApplication.class,
    UrlIntegrationTest.TestConfig.class // <-- include this explicitly
})
@Testcontainers
@ActiveProfiles("docker")
class UrlIntegrationTest {

    @Container
    @ServiceConnection
    static CassandraContainer<?> cassandra = new CassandraContainer<>("cassandra:4");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.cassandra.contact-points", cassandra::getHost);
        registry.add("spring.data.cassandra.port", cassandra::getFirstMappedPort);
        registry.add("spring.data.cassandra.local-datacenter", () -> "datacenter1");
        registry.add("spring.data.cassandra.keyspace-name", () -> "shortenit");
        registry.add("spring.data.cassandra.schema-action", () -> "CREATE_IF_NOT_EXISTS");
        registry.add("spring.kafka.bootstrap-servers", () -> "dummy:9092");
        registry.add("spring.kafka.listener.auto-startup", () -> "false");
    }
        

    @Autowired
    IUrlShorteningService service;

    @Autowired
    private CassandraAdminTemplate cassandraAdminTemplate;

    @PostConstruct
    public void logInit() {
        System.out.println("Loaded CassandraConfig");
    }

    @BeforeEach
    void ensureTableExists() {
        try {
            cassandraAdminTemplate.getCqlOperations().execute("SELECT * FROM urls LIMIT 1");
        } catch (Exception e) {
            cassandraAdminTemplate.createTable(true, UrlMapping.class);
        }
    }

    @Test
    void shortenAndFetch() {
        String shortUrl = service.shortenUrl("https://openai.com");
        Optional<String> resolved = service.getLongUrl(shortUrl);
        assertTrue(resolved.isPresent());
        assertEquals("https://openai.com", resolved.get());
    }

    @TestConfiguration
    static class TestConfig {
    
        @Bean
        @SuppressWarnings("unchecked")
        public KafkaTemplate<String, String> kafkaTemplate() {
            return Mockito.mock(KafkaTemplate.class);
        }
    
        @Bean
        public CassandraAdminTemplate cassandraAdminTemplate(org.springframework.data.cassandra.core.CassandraTemplate template) {
            return new CassandraAdminTemplate(template.getCqlOperations(), template.getConverter());
        }
    }
}
