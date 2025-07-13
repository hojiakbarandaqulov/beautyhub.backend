package com.example.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurationSource() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(
                                "http://localhost:63342",
                                "http://localhost:3000",
                                "http://localhost:3001",
                                "http://localhost:3002",
                                "https://beautyhubuz.uz",
                                "https://beautyhubuz-3d777984d358.herokuapp.com",
                                "http://localhost:3000",
                                "http://localhost:8080",
                                "*"
                        )
                        .maxAge(3600)
                        .allowedHeaders("*")
                        .allowedMethods("*");
            }
        };
    }
}
