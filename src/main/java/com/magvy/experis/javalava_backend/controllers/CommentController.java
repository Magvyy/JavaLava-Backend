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
    public ResponseEntity<CommentDTOResponse> CreateCommentHandler(@RequestBody CommentDTORequest commentDTO, Authentication auth) {
        User user = getLoggedInUser(auth);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return commentService.createPost(commentDTO, user);
    }
    @GetMapping("/post/{id}")
    public List<CommentDTOResponse> LoadCommentsHandler(@PathVariable int id, @RequestParam int page, Authentication auth) {
        User user = getLoggedInUser(auth);
        return commentService.loadCommentsByPost(page, id, user);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTOResponse> UpdateCommentsHandler(@PathVariable int id, @RequestBody CommentDTORequest commentDTORequest, Authentication auth) {
        User user = getLoggedInUser(auth);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return commentService.edit(id, user, commentDTORequest);
    }
    @DeleteMapping("/{id}")
    public HttpStatus DeleteCommentsHandler(@PathVariable int id, @RequestBody CommentDTORequest commentDTORequest, Authentication auth) {
        User user = getLoggedInUser(auth);
        if (user == null) return HttpStatus.UNAUTHORIZED;
        return commentService.delete(id, user, commentDTORequest);
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
