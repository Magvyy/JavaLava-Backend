package com.magvy.experis.javalava_backend.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
})

public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}