package com.konnect.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OauthConfig {

    @Bean
    public List<String> redirectWhitelist(
            @Value("${oauth.redirect.whitelist}") String whiteList
    ) {
        return List.of(whiteList.split("\\s*,\\s*"));
    }
}
