package com.konnect.config;


import com.konnect.auth.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import com.konnect.auth.jwt.CustomAuthenticationEntryPoint;
import com.konnect.auth.jwt.JWTFilter;
import com.konnect.auth.jwt.JWTUtil;
import com.konnect.auth.oauth2.CustomSuccessHandler;
import com.konnect.auth.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Environment env;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final List<String> clientUrlList;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    // 전체 Origin 허용
                    config.addAllowedOriginPattern("*");   // 혹은 config.setAllowedOriginPatterns(List.of("*"));

                    // 허용 HTTP 메서드 (필요 시 조정)
                    config.setAllowedMethods(
                            List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

                    // 모든 헤더 허용
                    config.setAllowedHeaders(List.of("*"));

                    // 자격 증명 포함 허용
                    config.setAllowCredentials(true);

                    // pre-flight 결과 캐싱 시간(초)
                    config.setMaxAge(3600L);

                    // Authorization 헤더 클라이언트에서 노출 허용 👇
                    config.setExposedHeaders(List.of("Authorization"));

                    return config;
                }));

        // 인증 오류 핸들링 추가(JWT 만료)
        http
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        //csrf disable(어차피 JWT 사용해서 stateless 상태로 관리할 것이므로 필요 X)
        http
                .csrf(AbstractHttpConfigurer::disable);

        //Form 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // 기본 로그아웃 방식 disable
        http
                .logout(AbstractHttpConfigurer::disable);

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        //JWTFilter 추가
        // (위치는 OAuth2LoginAuthenticationFilter 이후 등록)
        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // login Filter `/login`
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);

        // state 관련 param 받아서


        //oauth2 -> oauth2 를 통해, 인증 진행 `/oauth2/authorization/{provider}`
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                );
        //경로별 인가 작업
        http
                .authorizeHttpRequests(auth -> {
                    // 공통 허용 경로
                    auth.requestMatchers("/", "/login", "/signup", "/api/v1/all/**").permitAll();

                    // dev 환경일 때, Swagger 경로 허용
                    auth.requestMatchers(
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-resources/**",
                            "/webjars/**"
                    ).permitAll();

                    // 나머지는 인증 필요
                    auth.anyRequest().authenticated();
                });

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
