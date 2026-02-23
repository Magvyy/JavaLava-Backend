package com.magvy.experis.javalava_backend.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommentException extends RuntimeException {
    private final HttpStatus status;

    public CommentException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
