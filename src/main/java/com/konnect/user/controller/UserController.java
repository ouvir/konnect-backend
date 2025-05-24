package com.konnect.user.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.user.dto.UserDTO;
import com.konnect.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 정보", description = "사용자 정보 API")
@RestController
@RequestMapping("api/v1/user/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "현재 로그인 된 사용자 정보를 반환")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> myInfo(
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        // TODO userDetails.getId()의 3항연산자 모두 삭제
        UserDTO userInfo = userService.findById(userDetails == null ? 1 : userDetails.getId());
        return ResponseEntity.ok(userInfo);
    }

}
