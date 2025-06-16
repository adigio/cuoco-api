package com.cuoco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class CuocoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuocoApplication.class, args);
	}

}
