package com.magvy.experis.javalava_backend.domain.exceptions;

public class MissingUserException extends RuntimeException {
    public MissingUserException(String message) {
        super(message);
    }
}
