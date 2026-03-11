package com.ProyectoIntegradorBE.proaudioBE.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todas las rutas
                        .allowedOrigins("http://localhost:5173", "https://proaudiochannels.com.uy:8443",
                                "https://www.proaudiochannels.com.uy:8443") // Tu frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                        .allowedHeaders("*") // Permite todos los headers
                        .allowCredentials(true); // Permite cookies/autenticación si usas
            }
        };
    }
}

//ONLY FOR TEST ENVIRONMENT
//@Configuration
//public class CorsConfig {
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // Aplica a todas las rutas
//                        .allowedOrigins("http://localhost:5173") // Tu frontend
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
//                        .allowedHeaders("*") // Permite todos los headers
//                        .allowCredentials(true); // Permite cookies/autenticación si usas
//            }
//        };
//    }
//}