package com.cuoco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaRepositories
@EnableAsync
public class CuocoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuocoApplication.class, args);
	}

}
