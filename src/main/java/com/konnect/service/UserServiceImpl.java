package com.konnect.service;

import com.konnect.dto.UserDTO;
import com.konnect.entity.UserEntity;
import com.konnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
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
