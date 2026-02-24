package com.magvy.experis.javalava_backend.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AttachmentException extends RuntimeException {
    private final HttpStatus status;

    public AttachmentException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
