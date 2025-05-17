package com.cuoco.service.impl;

import com.cuoco.repository.IngredienteRepository;
import com.cuoco.repository.RecetaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@ComponentScan(basePackages = {"com.cuoco.service", "com.cuoco.repository", "com.cuoco.model"})
public class GeminiServiceImplTestConfig {

    @Bean
    @Primary
    public GeminiServiceImpl geminiService() {
        return new GeminiServiceImpl();
    }}
    
