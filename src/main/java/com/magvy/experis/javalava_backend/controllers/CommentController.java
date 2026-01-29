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

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<CommentDTOResponse> CreateCommentHandler(@RequestBody CommentDTORequest commentDTO, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return commentService.createPost(commentDTO, user);
    }
    @GetMapping("/post/{id}")
    public List<CommentDTOResponse> LoadCommentsHandler(@PathVariable Long id, @RequestParam int page, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        return commentService.loadCommentsByPost(page, id, user);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTOResponse> UpdateCommentsHandler(@PathVariable Long id, @RequestBody CommentDTORequest commentDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return commentService.edit(id, user, commentDTORequest);
    }
    @DeleteMapping("/{id}")
    public HttpStatus DeleteCommentsHandler(@PathVariable Long id, @RequestBody CommentDTORequest commentDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        if (user == null) return HttpStatus.UNAUTHORIZED;
        return commentService.delete(id, user, commentDTORequest);
    }

    private User getLoggedInUser(CustomUserDetails principal) {
        if (principal == null) return null;
        return principal.getUser();
    }
}
