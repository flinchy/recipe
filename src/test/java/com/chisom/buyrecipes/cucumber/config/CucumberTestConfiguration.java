package com.chisom.buyrecipes.cucumber.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class CucumberTestConfiguration {

    static final DockerImageName POSTGRES_IMAGE = DockerImageName
            .parse("postgres:latest")
            .asCompatibleSubstituteFor("postgres");

    @SuppressWarnings("resource")
    public static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>(POSTGRES_IMAGE)
                    .withDatabaseName("buyrecipe")
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withEnv("TZ", "UTC");

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {

        registry.add("JDBC_URL", POSTGRES::getJdbcUrl);
        registry.add("DB_USERNAME", POSTGRES::getUsername);
        registry.add("DB_PASSWORD", POSTGRES::getPassword);
    }
}
