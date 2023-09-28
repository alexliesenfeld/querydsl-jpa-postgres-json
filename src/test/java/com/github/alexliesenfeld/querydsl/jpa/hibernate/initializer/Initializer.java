package com.github.alexliesenfeld.querydsl.jpa.hibernate.initializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:15.3-alpine");
        dbContainer.start();

        TestPropertyValues.of(
                        "spring.datasource.url=" + dbContainer.getJdbcUrl(),
                        "spring.datasource.username=" + dbContainer.getUsername(),
                        "spring.datasource.password=" + dbContainer.getPassword(),
                        "spring.datasource.driverClassName=org.postgresql.Driver",
                        "spring.jpa.database-platform=org.hibernate.dialect.PostgresPlusDialect",
                        "spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true",
                        "spring.jpa.properties.hibernate.dialect=com.github.alexliesenfeld.querydsl.jpa.hibernate.PostgreSQLJsonDialect",
                        "spring.jpa.open-in-view=false")
                .applyTo(configurableApplicationContext.getEnvironment());
    }
}