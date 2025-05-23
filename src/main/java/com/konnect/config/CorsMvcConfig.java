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
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")           // 모든 엔드포인트
                .allowedOriginPatterns("*") // 모든 Origin 허용
                .allowedMethods("*")        // GET·POST·PUT … 전부 허용
                .allowedHeaders("*")        // 헤더 제한 없음
                .allowCredentials(true)     // 쿠키·인증 정보 포함 허용
                .exposedHeaders("Set-Cookie")// 프런트에서 응답 헤더 확인 가능
                .maxAge(3600);              // pre-flight 캐시 시간(초)
    }
}
