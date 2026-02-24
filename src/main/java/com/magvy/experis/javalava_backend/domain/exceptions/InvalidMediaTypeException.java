package com.magvy.experis.javalava_backend.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidMediaTypeException extends RuntimeException {
    private final HttpStatus status;

    public InvalidMediaTypeException(String message, HttpStatus status) {
        super("media type :" + message + "is not accepted");
        this.status = status;
    }
}
