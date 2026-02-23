package com.magvy.experis.javalava_backend.domain.exceptions;


import com.magvy.experis.javalava_backend.application.DTOs.outgoing.DefaultResponseDTO;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ErrorDTOResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DefaultResponseDTO> handleAuthenticationException(AuthenticationException exception) {
        return new ResponseEntity<>(new DefaultResponseDTO(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<DefaultResponseDTO> handleUnauthorizedDeletionException(UnauthorizedActionException exception) {
        return new ResponseEntity<>(new DefaultResponseDTO(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<DefaultResponseDTO> handleUserException(UserException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new DefaultResponseDTO(exception.getMessage()), headers, exception.getStatus());
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<DefaultResponseDTO> handlePostException(PostException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new DefaultResponseDTO(exception.getMessage()), headers, exception.getStatus());
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<DefaultResponseDTO> handleCommentException(CommentException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new DefaultResponseDTO(exception.getMessage()), headers, exception.getStatus());
    }

    @ExceptionHandler(LikeException.class)
    public ResponseEntity<DefaultResponseDTO> handleLikeException(LikeException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new DefaultResponseDTO(exception.getMessage()), headers, exception.getStatus());
    }

    @ExceptionHandler(FriendException.class)
    public ResponseEntity<DefaultResponseDTO> handleFriendException(FriendException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new DefaultResponseDTO(exception.getMessage()), headers, exception.getStatus());
    }

    @ExceptionHandler(FriendRequestException.class)
    public ResponseEntity<DefaultResponseDTO> handleFriendRequestException(FriendRequestException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new DefaultResponseDTO(exception.getMessage()), headers, exception.getStatus());
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<DefaultResponseDTO> handleMessageException(MessageException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new DefaultResponseDTO(exception.getMessage()), headers, exception.getStatus());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDTOResponse> handleException(ResponseStatusException exception, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ErrorDTOResponse error = new ErrorDTOResponse(
                exception.getStatusCode().value(),
                exception.getMessage()
        );
        return new ResponseEntity<>(error, headers, HttpStatus.UNAUTHORIZED);
    }
}
