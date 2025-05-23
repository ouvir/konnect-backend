package com.konnect.auth.service;

import com.konnect.auth.dto.SignUpDTO;
import com.konnect.user.entity.UserEntity;
import com.konnect.user.repository.UserRepository;
import com.konnect.auth.exception.SignUpError;
import com.konnect.auth.exception.SignUpRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private static final String UserRole = "0";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signUp(SignUpDTO signUpDTO) {
        String username = signUpDTO.getUsername();
        String email = signUpDTO.getEmail();
        String password = signUpDTO.getPassword();

        validateUser(email, username);

        UserEntity userEntity = new UserEntity(username, email, bCryptPasswordEncoder.encode(password), UserRole, null);
        userRepository.save(userEntity);
    }

    public void validateUser(String email, String name) {
        if (userRepository.existsByEmail(email)) {
            throw new SignUpRuntimeException(SignUpError.DUPLICATE_EMAIL);
        }
//        if (userRepository.existsByName(name)) {
//            throw new SignUpRuntimeException(SignUpError.DUPLICATE_USERNAME);
//        }
    }
}