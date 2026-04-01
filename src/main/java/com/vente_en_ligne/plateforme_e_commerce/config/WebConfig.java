package com.vente_en_ligne.plateforme_e_commerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ─── CORS ───
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // autorise tout
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // ─── Ressources statiques (images) ───
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:uploads/images/");
    }
}