package com.bright.logging_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class LoggingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoggingServiceApplication.class, args);
	}

}
