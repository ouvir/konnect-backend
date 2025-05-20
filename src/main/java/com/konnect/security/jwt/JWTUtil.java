package com.konnect.security.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    // 3시간
    private final long DEFAULT_EXPIRATION_TIME_MS = 60 * 60 * 3 * 1000L;
    public final int DEFAULT_EXPIRATION_TIME_SEC = (int) (DEFAULT_EXPIRATION_TIME_MS / 1000);
    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(Long userId, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    // 3시간 JWT 생성
    public String createJwt(Long userId, String role) {
        return createJwt(userId, role, DEFAULT_EXPIRATION_TIME_MS);
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(DEFAULT_EXPIRATION_TIME_SEC);
        //cookie.setSecure(true); //https만 가능하게 설정
        cookie.setPath("/");
        //cookie.setHttpOnly(true); //js가 cookie 못가져가도록 설정
        return cookie;
    }

    public Cookie deleteCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(0);
        //cookie.setSecure(true); //https만 가능하게 설정
        cookie.setPath("/");
        //cookie.setHttpOnly(true); //js가 cookie 못가져가도록 설정
        return cookie;
    }
}