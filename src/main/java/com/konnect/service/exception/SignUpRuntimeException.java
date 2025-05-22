package com.konnect.service.exception;

import lombok.Getter;

@Getter
public class SignUpRuntimeException extends RuntimeException {
    private final SignUpError signUpError;

    public SignUpRuntimeException(SignUpError signUpError) {
        super(signUpError.getMessage());
        this.signUpError = signUpError;
    }
}