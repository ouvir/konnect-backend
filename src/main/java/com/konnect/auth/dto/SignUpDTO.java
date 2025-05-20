package com.konnect.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpDTO {
    private String username;
    private String email;
    private String password;

    public SignUpDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}