package oauth.test.oauth2jwt.util;

import oauth.test.oauth2jwt.dto.UserDTO;
import oauth.test.oauth2jwt.entity.UserEntity;

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
