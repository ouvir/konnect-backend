package com.konnect.controller;

import com.konnect.dto.CustomUserPrincipal;
import com.konnect.dto.UserDTO;
import com.konnect.repository.UserRepository;
import com.konnect.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Tag(name = "사용자 정보", description = "사용자 정보 API")
@RestController
@RequestMapping("/user/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "현재 로그인 된 사용자 정보를 반환")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> myInfo(
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        UserDTO userInfo = userService.findById(userDetails.getId());
        return ResponseEntity.ok(userInfo);
    }

}
