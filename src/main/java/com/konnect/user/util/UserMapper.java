package com.konnect.user.util;

import com.konnect.user.dto.UserDTO;
import com.konnect.user.entity.UserEntity;

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
