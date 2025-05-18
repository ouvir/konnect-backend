package com.konnect.security.oauth2;

import com.konnect.dto.CustomUserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.konnect.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final List<String> redirectWhitelist;

    @Value("${client.url}")
    private String clientUrl;

    private static final String REDIRECT_COOKIE = "OAUTH2_REDIRECT_URI";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException, ServletException {

        //OAuth2User
        CustomUserPrincipal customUserDetails = (CustomUserPrincipal) authentication.getPrincipal();

        Long userId = customUserDetails.getId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(userId, role);

        // JWT Cookie에 담아 전달
        response.addCookie(jwtUtil.createCookie("Authorization", token));

        // redirect_uri 쿠키에서 값 얻기
        String redirectUri = getRedirectURL(request, response);
        response.sendRedirect(redirectUri); // 클라이언트 url TODO oauth 성공 시, url 등록
    }

    private String getRedirectURL(HttpServletRequest request, HttpServletResponse response) {
        String redirectUri = "";

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (REDIRECT_COOKIE.equals(cookie.getName())) {
                    redirectUri = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    // 화이트리스트 검증
                    if (redirectWhitelist.stream().noneMatch(redirectUri::startsWith)) {
                        redirectUri = clientUrl;
                    }
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        return redirectUri;
    }

}
