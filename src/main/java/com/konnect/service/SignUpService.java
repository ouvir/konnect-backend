package com.konnect.service;

import com.konnect.dto.SignUpDTO;
import com.konnect.entity.UserEntity;
import com.konnect.repository.UserRepository;
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

        if (userRepository.existsByname(username)) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        // TODO: 모든 회원가입한 유저는 기본적으로 일반 회원
        UserEntity userEntity = new UserEntity(username, email, bCryptPasswordEncoder.encode(password), UserRole, null);
        userRepository.save(userEntity);
    }
}