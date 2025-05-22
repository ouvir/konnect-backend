package com.konnect.user.service;

import com.konnect.user.dto.UserDTO;
import com.konnect.user.entity.UserEntity;
import com.konnect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(long id) {
        UserEntity entity = userRepository.findById(id);
        UserDTO dto = UserDTO.builder()
                .userId(id)
                .name(entity.getName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
        return dto;
    }
}
