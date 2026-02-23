package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.CommentDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.CommentDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.controllers.util.ResponseUtil;
import com.magvy.experis.javalava_backend.domain.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController extends BaseAuthController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<CommentDTOResponse> createComment(@PathVariable Long postId, @RequestBody CommentDTORequest commentDTO, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        CommentDTOResponse commentDTOResponse = commentService.createPost(postId, commentDTO);
        return ResponseUtil.wrapEntity(commentDTOResponse);
    }
    @GetMapping()
    public ResponseEntity<List<CommentDTOResponse>> getComments(@PathVariable Long postId, @RequestParam int offset) {
        List<CommentDTOResponse> commentDTOResponses = commentService.loadCommentsByPost(postId, offset);
        return ResponseUtil.wrapEntityList(commentDTOResponses);
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTOResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentDTORequest commentDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        CommentDTOResponse commentDTOResponse = commentService.edit(commentId, commentDTORequest);
        return ResponseUtil.wrapEntity(commentDTOResponse);
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        commentService.delete(commentId);
        return ResponseUtil.wrapEntity(null);
    }

}
