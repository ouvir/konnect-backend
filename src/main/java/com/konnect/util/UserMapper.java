package com.konnect.util;

import com.konnect.dto.UserDTO;
import com.konnect.entity.UserEntity;

public class UserMapper {
    public static UserDTO toDTO(UserEntity entity) {
        return UserDTO.builder()
                .userId(entity.getUserId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .oauthCode(entity.getOauthCode())
                .build();
    }
}
