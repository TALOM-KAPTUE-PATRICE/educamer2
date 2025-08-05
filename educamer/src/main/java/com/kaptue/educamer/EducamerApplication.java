package com.kaptue.educamer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Bean; // <-- AJOUTER
import org.springframework.web.client.RestTemplate; // <-- AJOUTER

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class EducamerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EducamerApplication.class, args);
	}

	

	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
