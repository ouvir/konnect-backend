package com.konnect.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpDTO {
    private String username;
    private String email;
    private String password;
}