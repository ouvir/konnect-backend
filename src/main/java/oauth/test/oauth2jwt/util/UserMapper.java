package oauth.test.oauth2jwt.util;

import oauth.test.oauth2jwt.dto.UserDTO;
import oauth.test.oauth2jwt.entity.UserEntity;

public class UserMapper {
    public static UserDTO toDTO(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .name(entity.getName())
                .role(entity.getRole())
                .build();
    }

    public static UserEntity toEntity(UserDTO dto) {
        return UserEntity.builder()
                .username(dto.getUsername())
                .name(dto.getName())
                .role(dto.getRole())
                .build();
    }
}
