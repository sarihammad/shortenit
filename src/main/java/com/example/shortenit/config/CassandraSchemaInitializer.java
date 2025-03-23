package com.example.shortenit.config;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("docker")
public class CassandraSchemaInitializer implements InitializingBean {

    private final CqlSession session;

    public CassandraSchemaInitializer(CqlSession session) {
        this.session = session;
    }

    @Override
    public void afterPropertiesSet() {
        log.info("Running CassandraSchemaInitializer to create table...");
        session.execute("""
            CREATE TABLE IF NOT EXISTS shortenit.urls (
                shorturl text PRIMARY KEY,
                longurl text
            );
        """);
        log.info("Table 'shortenit.urls' ensured.");
    }
}