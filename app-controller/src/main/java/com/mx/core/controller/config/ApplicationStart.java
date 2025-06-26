package com.mx.core.controller.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = { 
		"com.mx.core.common", 
		"com.mx.core.service",  
		"com.mx.core" })
@EnableJpaRepositories(basePackages = { "com.mx.core.repository" })
@EnableTransactionManagement
@EntityScan(basePackages = { "com.mx.core.repository.entity" })
public class ApplicationStart extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication sa = new SpringApplication(ApplicationStart.class);
		sa.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ApplicationStart.class);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
}
