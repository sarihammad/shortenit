package com.example.shortenit.config;

// import java.net.InetSocketAddress;
// import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
// import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.EnableCassandraAuditing;
import org.springframework.data.cassandra.config.SchemaAction;

import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

// import com.datastax.oss.driver.api.core.CqlSession;
// import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
// import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

@EnableCassandraRepositories(basePackages = "com.example.shortenit.repository")
@EnableCassandraAuditing
@Configuration
@Profile("docker")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.contact-points:localhost}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port:9042}")
    private int port;

    @Value("${spring.data.cassandra.local-datacenter:datacenter1}")
    private String localDatacenter;

    @Value("${spring.data.cassandra.keyspace-name:shortenit}")
    private String keyspace;

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Override
    public String getContactPoints() {
        return contactPoints;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getLocalDataCenter() {
        return localDatacenter;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Arrays.asList(
            CreateKeyspaceSpecification.createKeyspace(keyspace)
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication()
        );
    }

    // @Bean
    // @Primary
    // @ConditionalOnMissingBean(CqlSession.class)
    // public CqlSession cqlSession() {
    //     DriverConfigLoader loader = DriverConfigLoader.programmaticBuilder()
    //             .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(30))
    //             .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofSeconds(30))
    //             .withDuration(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT, Duration.ofSeconds(30))
    //             .build();

    //     // Step 1: Connect without keyspace
    //     CqlSession session = CqlSession.builder()
    //             .addContactPoint(new InetSocketAddress(contactPoints, port))
    //             .withLocalDatacenter(localDatacenter)
    //             .withConfigLoader(loader)
    //             .build();

    //     // Step 2: Create keyspace if it doesn't exist
    //     session.execute(String.format(
    //         "CREATE KEYSPACE IF NOT EXISTS %s WITH replication = {'class':'SimpleStrategy','replication_factor':1} AND durable_writes = true;",
    //         keyspace
    //     ));

    //     // Step 3: Close session and reconnect with keyspace
    //     session.close();

    //     return CqlSession.builder()
    //             .addContactPoint(new InetSocketAddress(contactPoints, port))
    //             .withLocalDatacenter(localDatacenter)
    //             .withKeyspace(keyspace)
    //             .withConfigLoader(loader)
    //             .build();
    // }

}