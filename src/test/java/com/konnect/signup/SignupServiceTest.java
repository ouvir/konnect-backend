package com.konnect.signup;

import com.konnect.dto.SignUpDTO;
import com.konnect.entity.UserEntity;
import com.konnect.repository.UserRepository;
import com.konnect.service.SignUpService;
import com.konnect.service.exception.SignUpErrorMessages;
import com.konnect.service.exception.SignUpRuntimeException;
import org.antlr.v4.runtime.misc.LogManager;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
}
