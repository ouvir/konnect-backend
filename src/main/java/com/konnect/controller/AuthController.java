package com.konnect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Tag(name = "인증", description = "로그인 및 인증 관련 API")
public class AuthController {

    @PostMapping("/login")
    @Operation(
            summary = "일반 로그인",
            description = "이메일과 비밀번호로 로그인 후 JWT 쿠키를 발급받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공(JWT 발급)",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "JWT 쿠키 예시",
                                            summary = "JWT는 쿠키에 담겨 응답",
                                            value = """
                                Set-Cookie: accessToken=eyJhbGciOiJIUzI1NiJ9...; Path=/; Max-Age=10800;
                                JWT Payload:
                                {
                                  "userId": 1,
                                  "role": "ROLE_USER",
                                  "exp": 1715620000
                                }
                                """
                                    )
                            )
                    ),

                    @ApiResponse(responseCode = "401", description = "로그인 실패 - 잘못된 자격 증명",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"error\": \"Unauthorized\"}")
                            )
                    )
            }
    )
    public void login(@RequestBody LoginRequest request) {
        // Swagger에서 테스트 가능하게 Dummy 컨트롤러 구현 (실제 처리는 Filter)
    }

    @Operation(
            summary = "OAuth 로그인 리다이렉트",
            description = """
                    프론트엔드에서 `/oauth2/authorization/{provider}` (예: google, kakao, naver)로 리디렉트 요청을 보내면
                    소셜 로그인 인증이 시작되고, 인증 성공 시 JWT 쿠키가 발급됩니다.

                    - 예: `/oauth2/authorization/kakao`
                    """
    )
    @GetMapping("/oauth2/authorization/{provider}")
    public void oauthLogin(@PathVariable String provider) {
        // Swagger 문서화용 Dummy endpoint
    }
}

@Data
@Schema(description = "일반 로그인 요청")
class LoginRequest {

    @Schema(description = "이메일", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "비밀번호", example = "P@ssw0rd123", required = true)
    private String password;
}
