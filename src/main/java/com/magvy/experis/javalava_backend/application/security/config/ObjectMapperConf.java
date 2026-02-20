package com.magvy.experis.javalava_backend.application.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConf {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
