package com.dfn.dump_analyzer_backend.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("POST","GET","UPDATE","DELETE")
                .allowCredentials(true);
    }
}
