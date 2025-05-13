package com.konnect.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SignUpError {

    DUPLICATE_USERNAME(HttpStatus.CONFLICT, SignUpErrorMessages.EXISTED_NAME),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, SignUpErrorMessages.EXISTED_EMAIL);

    private final HttpStatus httpStatus;
    private final String message;
}