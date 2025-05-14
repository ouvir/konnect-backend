package com.konnect.controller;

import com.konnect.dto.SignUpDTO;
import com.konnect.service.SignUpService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @Operation(summary = "회원가입", tags = {"인증"})
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO joinDTO) {
        try {
            signUpService.signUp(joinDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
