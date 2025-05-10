package com.konnect.dto;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "사용자 정보 DTO")
public class UserDTO {

    @Schema(description = "사용자 고유 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "비밀번호 (요청에만 사용)", example = "P@ssw0rd123")
    private String password;

    @Schema(description = "사용자 권한", example = "ROLE_USER")
    private String role;

    @Schema(description = "OAuth 로그인 제공자 코드 (예: naver, kakao, google)", example = "kakao@1jljd1ss124")
    private String oauthCode;
}
