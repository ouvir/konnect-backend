package com.konnect.security;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.user.dto.UserDTO;
import com.konnect.auth.jwt.JWTUtil;
import com.konnect.auth.oauth2.CustomSuccessHandler;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class Oauth2LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomSuccessHandler successHandler;

    @MockitoBean
    private JWTUtil jwtUtil;

    @Test
    @DisplayName("네이버 OAuth 요청 redirect 확인")
    void whenRequestingNaverAuthorization_thenRedirectsToNaverAuthorizeEndpoint() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/naver"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", startsWith("https://nid.naver.com/oauth2.0/authorize")));
    }

    @Test
    @DisplayName("카카오 OAuth 요청 redirect 확인")
    void whenRequestingKakaoAuthorization_thenRedirectsToNaverAuthorizeEndpoint() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/kakao"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", startsWith("https://kauth.kakao.com/oauth/authorize")));
    }

    @Test
    @DisplayName("구글 OAuth 요청 redirect 확인")
    void whenRequestingGoogleAuthorization_thenRedirectsToGoogleAuthorizeEndpoint() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", startsWith("https://accounts.google.com/o/oauth2/")));
    }
    @Test
    @DisplayName("OAuth 인증 성공시, jwt 쿠키 반환 및 redirect 확인")
    void onAuthenticationSuccess_shouldSetJwtCookieAndRedirect() throws Exception {
        // given
        MockHttpServletRequest request  = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Long   userId = 4122L;
        String role   = "ROLE_USER";

        // principal 준비
        UserDTO userDto = UserDTO.builder()
                .userId(userId)
                .role(role)
                .build();
        CustomUserPrincipal principal = new CustomUserPrincipal(userDto);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );

        // JWTUtil 동작 모킹
        when(jwtUtil.createJwt(userId, role)).thenReturn("mock-jwt-token");
        Cookie mockCookie = new Cookie("Authorization", "mock-jwt-token");
        when(jwtUtil.createCookie("Authorization", "mock-jwt-token"))
                .thenReturn(mockCookie);

        // when
        successHandler.onAuthenticationSuccess(request, response, auth);

        // then
        // 1) 쿠키가 응답에 담겨야 한다
        Cookie cookie = response.getCookie("Authorization");
        assertNotNull(cookie, "Authorization 쿠키가 생성되어야 합니다");
        assertEquals("mock-jwt-token", cookie.getValue(), "쿠키 값이 createJwt 결과여야 합니다");

        // 2) 설정된 client.url로 리다이렉트되어야 한다
        assertEquals("http://localhost:5173", response.getRedirectedUrl());
    }
}
