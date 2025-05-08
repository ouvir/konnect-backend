package oauth.test.oauth2jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String role;
    private String name;
    private String username;
}
