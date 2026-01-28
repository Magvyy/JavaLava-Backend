package com.magvy.experis.javalava_backend.domain.exceptions;

public class MissingPostException extends RuntimeException {
    public MissingPostException(String message) {
        super(message);
    }
}
