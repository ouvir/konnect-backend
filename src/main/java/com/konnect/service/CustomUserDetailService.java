package com.konnect.service;

import com.konnect.dto.CustomUserPrincipal;
import com.konnect.entity.UserEntity;
import com.konnect.repository.UserRepository;
import com.konnect.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            System.out.println("userNull");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }

        //UserDetails에 담아 return하면 AutneticationManager가 검증
        return new CustomUserPrincipal(UserMapper.toDTO(user));
    }
}