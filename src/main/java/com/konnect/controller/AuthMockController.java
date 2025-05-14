package com.konnect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;


@RestController
@Profile("dev")
@Tag(name = "인증", description = "인증 관련 API(로그인, 회원가입)")
public class AuthMockController {

    @PostMapping("/login")
    @Operation(
            summary = "일반 로그인 요청",
            description = "이메일과 비밀번호로 로그인하여, JWT 쿠키를 발급받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공(JWT 발급)",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Authorization",
                                            summary = "userId 와 role 정보로 acceessToken을 만들어 반환",
                                            value = "eyJhbGciOiJIUz..."
                                    )
                            )
                    ),

                    @ApiResponse(responseCode = "401", description = "로그인 실패(잘못된 자격 증명)",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "status": 401,
                                                        "error": "Unauthorized",
                                                        "message": "인증되지 않았거나 만료된 토큰입니다.",
                                                        "path": "{url}/login"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    public void login(@RequestBody LoginRequest request) {
        // Swagger에서 테스트 가능하게 Dummy 컨트롤러 구현 (실제 처리는 Filter)
    }

    @Operation(
            summary = "OAuth 로그인 요청",
            description = """
                    경로로 redirect 요청시, 소셜 로그인 인증이 시작되며 인증 성공 시 JWT 쿠키가 발급됩니다.
                    """
    )
    @GetMapping("/oauth2/authorization/{provider}")
    public void oauthLogin(@PathVariable @Schema(description = "provider", example = "naver") String provider) {
        // Swagger 문서화용 Dummy endpoint
    }

    @Data
    @Schema(description = "일반 로그인 요청 데이터")
    class LoginRequest {

        @Schema(description = "이메일", example = "user@example.com", required = true)
        private String email;

        @Schema(description = "비밀번호", example = "P@ssw0rd123", required = true)
        private String password;
    }
}