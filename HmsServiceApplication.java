package com.emi_hms_microservice.hms_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HmsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmsServiceApplication.class, args);
    }
}
