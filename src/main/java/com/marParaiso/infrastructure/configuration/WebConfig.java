package com.marParaiso.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")  // Configura CORS para todas las rutas bajo /api
                .allowedOrigins("http://localhost:4200") // Permite solicitudes desde el frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("Authorization", "Content-Type", "Accept") // Encabezados permitidos
                .allowCredentials(true); // Permite envío de cookies o tokens
    }
}

