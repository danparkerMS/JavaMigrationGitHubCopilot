package com.nytour.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Legacy Spring Boot 2.7.x Application
 * 
 * Migration Notes:
 * - This is Spring Boot 2.7.x (legacy, last version before 3.x)
 * - Still uses javax.* packages (not jakarta.*)
 * - Target migration: Spring Boot 3.x (uses jakarta.*, requires JDK 17+)
 * - @SpringBootApplication replaces XML configuration
 * - @EnableScheduling activates @Scheduled tasks
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
