package com.konnect.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnect.auth.dto.CustomUserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        super.setAuthenticationManager(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

        // 로그인 URL 변경
        setFilterProcessesUrl("/api/v1/all/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response
    ) throws AuthenticationException {

        try {
            // JSON 요청 본문에서 email, password 추출
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);

            String email = credentials.get("email");
            String password = credentials.get("password");

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password, null);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("[login]: JSON 요청 파싱 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException {
        CustomUserPrincipal customUserDetails = (CustomUserPrincipal) authentication.getPrincipal();

        Long userId = customUserDetails.getId();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // JWT 발급
        String token = jwtUtil.createJwt(userId, role);

        // [옵션 1] Authorization 헤더에 담기
        response.setHeader("Authorization", "Bearer " + token);

        // [옵션 2] JSON 바디로 응답
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String json = String.format("""
        {
            "status": 200,
            "message": "로그인 성공",
            "token": "%s",
            "userId": %d
        }
        """, token, userId);

        response.getWriter().write(json);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
