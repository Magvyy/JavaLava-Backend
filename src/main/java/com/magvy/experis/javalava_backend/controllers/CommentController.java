package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.CommentDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.PostDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO.CommentDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.CommentService;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    public CommentController(CommentService commentService, UserRepository userRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public HttpStatus CreateCommentHandler(@RequestBody CommentDTORequest commentDTO, @RequestParam Authentication auth) {
        User user = getLoggedInUser(auth);
        if (user == null) return HttpStatus.UNAUTHORIZED;
        if (commentService.createPost(commentDTO, user)) return HttpStatus.OK;
        else return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    @GetMapping("/{postId}")
    public List<CommentDTOResponse> LoadCommentsHandler(@PathVariable int postId, @RequestParam int page, @RequestParam (required = false) Authentication auth) {
        User user = getLoggedInUser(auth);
        return commentService.loadCommentsByPost(page, postId, user);
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTOResponse> UpdateCommentsHandler(@PathVariable int commentId, @RequestBody CommentDTORequest commentDTORequest, @RequestParam Authentication auth) {
        User user = getLoggedInUser(auth);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return commentService.edit(commentId, user, commentDTORequest);
    }
    @DeleteMapping("/{commentId}")
    public HttpStatus DeleteCommentsHandler(@PathVariable int commentId, @RequestBody CommentDTORequest commentDTORequest, @RequestParam Authentication auth) {
        User user = getLoggedInUser(auth);
        if (user == null) return HttpStatus.UNAUTHORIZED;
        return commentService.delete(commentId, user, commentDTORequest);
    }

    private User getLoggedInUser(Authentication auth) {
        Object principal = auth.getPrincipal();
        if (principal instanceof String username) {
            Optional<User> oUser = userRepository.findByUsername(username);
            if (oUser.isEmpty()) {
                return null;
            }
            return oUser.get();
        }
        return null;
    }
}
