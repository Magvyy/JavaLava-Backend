package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.CommentDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.CommentDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post/{postId}/comments")
public class CommentController extends BaseAuthHController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<CommentDTOResponse> createComment(@PathVariable Long postId, @RequestBody CommentDTORequest commentDTO, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return commentService.createPost(postId, user, commentDTO);
    }
    @GetMapping()
    public List<CommentDTOResponse> getComments(@PathVariable Long postId, @RequestParam int page, @AuthenticationPrincipal CustomUserDetails principal) {
        Optional<User> user = getUserIfAuth(principal);
        return commentService.loadCommentsByPost(postId, user, page);
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTOResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentDTORequest commentDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return commentService.edit(commentId, user, commentDTORequest);
    }
    @DeleteMapping("/{commentId}")
    public HttpStatus deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        if (user == null) return HttpStatus.UNAUTHORIZED;
        return commentService.delete(commentId, user);
    }

}
