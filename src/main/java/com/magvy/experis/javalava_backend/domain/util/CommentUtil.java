package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.CommentDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Comment;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.CommentException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CommentUtil {
    private final CommentRepository commentRepository;
    private final SecurityUtil securityUtil;

    public CommentUtil(CommentRepository commentRepository, SecurityUtil securityUtil) {
        this.commentRepository = commentRepository;
        this.securityUtil = securityUtil;
    }

    public Comment convertToEntity(CommentDTORequest commentDTO, Post post) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        return new Comment(
                commentDTO.getContent(),
                post,
                authenticatedUser
        );
    }

    public Comment findByIdOrThrow(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CommentException("Comment not found", HttpStatus.NOT_FOUND));
    }

    public void validate(CommentDTORequest commentDTORequest) {
        if (!isValidContent(commentDTORequest.getContent())) throw new CommentException("Invalid content", HttpStatus.BAD_REQUEST);
    }

    private boolean isValidContent(String content) {
        return !content.trim().isEmpty();
    }

    public boolean authenticatedUserOwnsComment(Comment comment) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        return authenticatedUser.getId().equals(comment.getUser().getId());
    }
}
