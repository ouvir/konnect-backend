package com.konnect.diary.service.exception;

public class FileStorageRuntimeException extends RuntimeException {
    public FileStorageRuntimeException(String message, Exception e) {
        super(message);
    }
}
