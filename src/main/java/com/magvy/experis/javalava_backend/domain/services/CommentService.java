package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.CommentDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.CommentDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Comment;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.CommentException;
import com.magvy.experis.javalava_backend.domain.exceptions.UserException;
import com.magvy.experis.javalava_backend.domain.exceptions.UnauthorizedActionException;
import com.magvy.experis.javalava_backend.domain.util.CommentUtil;
import com.magvy.experis.javalava_backend.domain.util.PostUtil;
import com.magvy.experis.javalava_backend.domain.util.SecurityUtil;
import com.magvy.experis.javalava_backend.domain.util.UserUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentUtil commentUtil;
    private final SecurityUtil securityUtil;
    private final PostUtil postUtil;
    private final WebSocketService webSocketService;
    private final int pageSize = 10;

    public CommentService(CommentRepository commentRepository, CommentUtil commentUtil, SecurityUtil securityUtil, PostUtil postUtil, WebSocketService webSocketService) {
        this.commentRepository = commentRepository;
        this.commentUtil = commentUtil;
        this.securityUtil = securityUtil;
        this.postUtil = postUtil;
        this.webSocketService = webSocketService;
    }


    public CommentDTOResponse createPost(Long postId, CommentDTORequest commentDTORequest) {
        commentUtil.validate(commentDTORequest);

        Post post = postUtil.findByIdOrThrow(postId);
        if(!postUtil.isPostVisibleToAuthenticatedUser(post)) throw new CommentException("User not authorized to create comments on this post", HttpStatus.FORBIDDEN);

        Comment comment = commentUtil.convertToEntity(commentDTORequest, post);
        comment = commentRepository.save(comment);
        webSocketService.sendNotification(
                post.getUser().getUserName(),
                "New comment on your post from " + comment.getUser().getUserName()
        );

        return new CommentDTOResponse(comment);
    }

    public List<CommentDTOResponse> loadCommentsByPost(Long postId, int offset) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);

        Post post = postUtil.findByIdOrThrow(postId);
        if (!securityUtil.isAuthenticated()) {
            if (!post.isVisible()) {
                throw new CommentException("User not authorized to view comments on this post", HttpStatus.FORBIDDEN);
            }
        }
        if (!postUtil.isPostVisibleToAuthenticatedUser(post)) {
            throw new UserException("User not authorized to view comments on this post", HttpStatus.FORBIDDEN);
        }

        List<Comment> comments = commentRepository.findByPost(post, pageable);
        return comments.stream().map(CommentDTOResponse::new).toList();
    }

    public CommentDTOResponse edit(Long commentId, CommentDTORequest commentDTORequest) {
        commentUtil.validate(commentDTORequest);
        Comment comment = commentUtil.findByIdOrThrow(commentId);
        if (!commentUtil.authenticatedUserOwnsComment(comment)) throw new CommentException("User does not have permission to edit this comment", HttpStatus.FORBIDDEN);
        comment.setContent(commentDTORequest.getContent());
        comment = commentRepository.save(comment);
        return new CommentDTOResponse(comment);
    }

    public void delete(Long commentId) {
        Comment comment = commentUtil.findByIdOrThrow(commentId);
        if (!commentUtil.authenticatedUserOwnsComment(comment) && !securityUtil.authenticatedUserIsAdmin()) throw new CommentException("User does not have permission to delete this comment", HttpStatus.FORBIDDEN);
        commentRepository.delete(comment);
    }
}
