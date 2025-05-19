package com.konnect.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CorsMvcConfig implements WebMvcConfigurer {

    private final List<String> clientUrlList;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        for (String clientUrl : clientUrlList) {
            corsRegistry.addMapping("/**")
                    .exposedHeaders("Set-Cookie")
                    .allowedOrigins(clientUrl);
        }
    }
}
