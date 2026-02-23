package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PermissionsDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PostDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.controllers.util.ResponseUtil;
import com.magvy.experis.javalava_backend.domain.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController extends BaseAuthController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    public ResponseEntity<PostDTOResponse> createPost(@RequestBody PostDTORequest postDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        PostDTOResponse postDTOResponse = postService.createPost(postDTORequest);
        return ResponseUtil.wrapEntity(postDTOResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<PostDTOResponse> getPost(@PathVariable Long id) {
        PostDTOResponse postDTOResponse = postService.readPost(id);
        return ResponseUtil.wrapEntity(postDTOResponse);
    }

    @GetMapping("{id}/perms")
    public ResponseEntity<PermissionsDTOResponse> getPostPermissions(@PathVariable Long id) {
        PermissionsDTOResponse perms = postService.getPermissions(id);
        return ResponseUtil.wrapEntity(perms);
    }

    @PutMapping("{id}")
    public ResponseEntity<PostDTOResponse> updatePost(@PathVariable Long id, @RequestBody PostDTORequest postDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        PostDTOResponse postDTOResponse = postService.updatePost(id, postDTORequest);
        return ResponseUtil.wrapEntity(postDTOResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        postService.deletePost(id);
        return ResponseUtil.wrapEntity(null);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PostDTOResponse>> loadPostByUserHandler(@PathVariable Long id, @RequestParam int offset) {
        List<PostDTOResponse> postDTOResponses = postService.loadPostsByUser(offset, id);
        return ResponseUtil.wrapEntityList(postDTOResponses);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTOResponse>> loadPostHandler(@RequestParam int offset) {
        List<PostDTOResponse> postDTOResponses = postService.loadPosts(offset);
        return ResponseUtil.wrapEntityList(postDTOResponses);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<PostDTOResponse>> loadPostByFriendsHandler(@RequestParam int offset, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        List<PostDTOResponse> postDTOResponses = postService.loadPostsByFriends(offset);
        return ResponseUtil.wrapEntityList(postDTOResponses);
    }
}