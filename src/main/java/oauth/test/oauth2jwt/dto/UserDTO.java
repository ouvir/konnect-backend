package oauth.test.oauth2jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String role;
    private String name;
    private String username;
    //TODO: 필요시 email field 추가
}
