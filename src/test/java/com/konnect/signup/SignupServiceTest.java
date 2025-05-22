package com.konnect.signup;

import com.konnect.auth.dto.SignUpDTO;
import com.konnect.user.repository.UserRepository;
import com.konnect.auth.service.SignUpService;
import com.konnect.auth.exception.SignUpErrorMessages;
import com.konnect.auth.exception.SignUpRuntimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SignupServiceTest {

    private static final String NAME = "signUpTestUserName";
    private static final String EMAIL = "signUpTestUser@test.com";
    private static final String PASSWORD = "signUpTestUserPassword";

    @InjectMocks
    SignUpService signUpService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("중복되는 이메일이 존재하면 에러를 반환한다.")
    void validateDuplicateUser_throwException_IfEmailAlreadyExists() {
        // Given
        given(userRepository.existsByEmail(EMAIL)).willReturn(true);

        // Then
        assertThatThrownBy(() -> signUpService.validateUser(EMAIL, NAME))
                .isInstanceOf(SignUpRuntimeException.class)
                .hasMessage(SignUpErrorMessages.EXISTED_EMAIL);
    }

    @Test
    @DisplayName("중복되는 유저명이 존재하면 에러를 반환한다.")
    void validateDuplicateUser_throwException_IfNameAlreadyExists() {
        // Given
        given(userRepository.existsByName(NAME)).willReturn(true);

        // Then
        assertThatThrownBy(() -> signUpService.validateUser(EMAIL, NAME))
                .isInstanceOf(SignUpRuntimeException.class)
                .hasMessage(SignUpErrorMessages.EXISTED_NAME);
    }

    @Test
    @DisplayName("이메일과 닉네임이 중복되지 않으면 회원가입이 성공한다")
    void signUp_validateUser_IfSaveSuccess() {
        // Given
        SignUpDTO signUpDTO = new SignUpDTO(NAME, EMAIL, PASSWORD);

        given(userRepository.existsByEmail(EMAIL)).willReturn(false);
        given(userRepository.existsByName(NAME)).willReturn(false);

        // Then
        assertThatCode(() -> signUpService.signUp(signUpDTO))
                .doesNotThrowAnyException();
    }

}
