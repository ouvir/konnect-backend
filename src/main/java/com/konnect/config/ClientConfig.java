package com.konnect.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ClientConfig {

    @Bean
    public List<String> clientUrlList(
            @Value("${client.url}") String clientUrls
    ) {
        return List.of(clientUrls.split("\\s*,\\s*"));
    }
}
