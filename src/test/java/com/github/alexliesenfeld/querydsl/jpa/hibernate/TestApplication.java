package com.github.alexliesenfeld.querydsl.jpa.hibernate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Import(value = {}
)
@Configuration
@EntityScan("com.github.alexliesenfeld.querydsl.jpa.hibernate")
@EnableJpaRepositories(basePackages = "com.github.alexliesenfeld.querydsl.jpa.hibernate")
@EnableTransactionManagement
@TestPropertySource
//@EnableAspectJAutoProxy
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}