package com.konnect.signup;

import com.konnect.dto.SignUpDTO;
import com.konnect.entity.UserEntity;
import com.konnect.repository.UserRepository;
import com.konnect.service.SignUpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
public class SignUpServiceTests {
    private static final String NAME = "signUpTestUserName";
    private static final String EMAIL = "signUpTestUser@test.com";
    private static final String PASSWORD = "signUpTestUserPassword";

    @Autowired
    SignUpService signUpService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공시 DTO로 전달된 데이터와 저장된 데이터를 검증한다.")
    void signUp_compareUser_WithSavedUserInDB() {
        // given
        SignUpDTO signUpDto = new SignUpDTO(NAME, EMAIL, PASSWORD);
        String userRole = "0";

        // when
        signUpService.signUp(signUpDto);

        // then
        UserEntity savedUser = userRepository.findByEmail(EMAIL);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(NAME);
        assertThat(savedUser.getEmail()).isEqualTo(EMAIL);
        assertThat(passwordEncoder.matches(PASSWORD, savedUser.getPassword())).isTrue();
        assertThat(savedUser.getRole()).isEqualTo(userRole);

    }
}