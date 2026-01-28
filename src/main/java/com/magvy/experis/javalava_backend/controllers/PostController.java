package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO.PostDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.PostDTORequest;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.PostService;
import com.magvy.experis.javalava_backend.infrastructure.readonly.ReadOnlyUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PostDTOResponse;
import com.magvy.experis.javalava_backend.application.custom.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.PostService;
import com.magvy.experis.javalava_backend.infrastructure.readonly.ReadOnlyUserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final AuthenticationManager authenticationManager;

    public PostController(PostService postService, AuthenticationManager authenticationManager) {
        this.postService = postService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping()
    public ResponseEntity<PostDTOResponse> createPost(@RequestBody PostDTORequest postDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        Post post = postService.createPost(user, postDTORequest);
        return new ResponseEntity<>(new PostDTOResponse(post), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<PostDTOResponse> getPost(@PathVariable("id") int id, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        Optional<Post> oPost = postService.getPost(user, id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (oPost.isEmpty()) {
            return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new PostDTOResponse(oPost.get()), HttpStatus.OK);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<PostDTOResponse> updatePost(@PathVariable("id") int id, @RequestBody PostDTORequest postDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        Post post = postService.updatePost(user, postDTORequest);
        return new ResponseEntity<>(new PostDTOResponse(post), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<PostDTOResponse> deletePost(@PathVariable("id") int id, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        postService.deletePost(user, id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public List<PostDTOResponse> LoadPostByUserHandler(@PathVariable int id, @RequestParam int page, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        return postService.loadPostsByUser(page, user, id);
    }

    @GetMapping("/all")
    public List<PostDTOResponse> LoadPostHandler(@RequestParam int page, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        return postService.loadPosts(page, user);
    }

    @GetMapping("/friends")
    public List<PostDTOResponse> LoadPostByFriendsHandler(@RequestParam int page, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        return postService.loadPostsByFriends(page, user);
    }

    private User getLoggedInUser(CustomUserDetails principal) {
        if (principal == null) return null;
        return principal.getUser();
    }
}