package com.magvy.experis.javalava_backend.domain.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Authentication Failed.");
        errorResponse.put("message", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Registration Failed.");
        errorResponse.put("message", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingPostException.class)
    public ResponseEntity<Map<String, String>> handleMissingPostException(MissingPostException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Post not found.");
        errorResponse.put("message", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedDeletionException(UnauthorizedActionException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized Deletion.  ");
        errorResponse.put("message", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
