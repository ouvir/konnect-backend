package com.konnect.auth.jwt;

import com.konnect.auth.dto.CustomUserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.konnect.user.dto.UserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        // 1️⃣ 헤더에서 Authorization 추출
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.debug("Authorization 헤더가 없거나 형식이 잘못됨");
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ JWT 추출
        String token = authorizationHeader.substring(7); // "Bearer " 이후 토큰

        // 3️⃣ 토큰 만료 여부 확인
        if (jwtUtil.isExpired(token)) {
            throw new ExpiredJwtException(null, null, "JWT expired");
        }

        // 4️⃣ 토큰에서 사용자 정보 추출
        Long userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);

        UserDTO userDTO = UserDTO.builder()
                .userId(userId)
                .role(role)
                .build();

        CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(userDTO);

        // 5️⃣ Security 인증 정보 등록
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserPrincipal,
                null,
                customUserPrincipal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}
