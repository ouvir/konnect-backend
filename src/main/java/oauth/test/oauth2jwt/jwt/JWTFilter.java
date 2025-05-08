package oauth.test.oauth2jwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oauth.test.oauth2jwt.dto.CustomOAuth2User;
import oauth.test.oauth2jwt.dto.UserDTO;
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
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            log.debug("Cookie: {}", cookie);
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        // Authorization Header 검증
        if (authorization == null) {
            log.debug("token null");
            filterChain.doFilter(request, response);
            return;
        }

        // Token
        String token = authorization;

        // Token ExpiredTime 검증
        if (jwtUtil.isExpired(token)) {
            log.debug("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // get username & role from Token
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // init userDTO
        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .role(role)
                .build();

        // put userDTO to UserDetails
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        // security 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customOAuth2User,
                null,
                customOAuth2User.getAuthorities()
        );

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
