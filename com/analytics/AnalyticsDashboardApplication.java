package com.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot Application Entry Point for Analytics Dashboard
 * Starts the embedded Tomcat server and initializes the application
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.analytics",
    "dashboard",
    "dto",
    "service",
    "engine",
    "repository",
    "exception",
    "model",
    "mapper",
    "internal"
})
public class AnalyticsDashboardApplication {

    /**
     * Main entry point for the application
     * 
     * @param args Command-line arguments (optional port override via --server.port=XXXX)
     */
    public static void main(String[] args) {
        SpringApplication.run(AnalyticsDashboardApplication.class, args);
    }
}
