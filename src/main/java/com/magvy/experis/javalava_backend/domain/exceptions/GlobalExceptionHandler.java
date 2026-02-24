package com.magvy.experis.javalava_backend.domain.exceptions;


import com.magvy.experis.javalava_backend.application.DTOs.outgoing.DefaultResponseDTO;
import com.magvy.experis.javalava_backend.controllers.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<DefaultResponseDTO> handleUserException(UserException exception) {
        return ResponseUtil.wrapEntity(new DefaultResponseDTO(exception.getMessage()), exception.getStatus());
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<DefaultResponseDTO> handlePostException(PostException exception) {
        return ResponseUtil.wrapEntity(new DefaultResponseDTO(exception.getMessage()), exception.getStatus());
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<DefaultResponseDTO> handleCommentException(CommentException exception) {
        return ResponseUtil.wrapEntity(new DefaultResponseDTO(exception.getMessage()), exception.getStatus());
    }

    @ExceptionHandler(LikeException.class)
    public ResponseEntity<DefaultResponseDTO> handleLikeException(LikeException exception) {
        return ResponseUtil.wrapEntity(new DefaultResponseDTO(exception.getMessage()), exception.getStatus());
    }

    @ExceptionHandler(FriendException.class)
    public ResponseEntity<DefaultResponseDTO> handleFriendException(FriendException exception) {
        return ResponseUtil.wrapEntity(new DefaultResponseDTO(exception.getMessage()), exception.getStatus());
    }

    @ExceptionHandler(FriendRequestException.class)
    public ResponseEntity<DefaultResponseDTO> handleFriendRequestException(FriendRequestException exception) {
        return ResponseUtil.wrapEntity(new DefaultResponseDTO(exception.getMessage()), exception.getStatus());
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<DefaultResponseDTO> handleMessageException(MessageException exception) {
        return ResponseUtil.wrapEntity(new DefaultResponseDTO(exception.getMessage()), exception.getStatus());
    }
}
