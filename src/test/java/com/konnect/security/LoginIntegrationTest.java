package com.konnect.security;

import com.konnect.entity.UserEntity;
import com.konnect.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 테스트 사용자 저장
        UserEntity user = UserEntity.builder()
                .email("test@konnect.com")
                .password(passwordEncoder.encode("test1234"))
                .name("테스트유저")
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("로그인 성공 - JWT 쿠키 발급 확인")
    void loginSuccess() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                    {
                                      "email": "test@konnect.com",
                                      "password": "test1234"
                                    }
                                """
                        ).accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(cookie().exists("Authorization"));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void loginFailWrongPassword() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                    {
                                      "email": "test@konnect.com",
                                      "password": "wrongpassword"
                                    }
                                """
                        )
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }
}
