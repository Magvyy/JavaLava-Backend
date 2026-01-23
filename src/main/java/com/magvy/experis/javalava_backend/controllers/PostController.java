package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.PostDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO.PostDTORequest;
import com.magvy.experis.javalava_backend.domain.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public HttpStatus LoginPostHandler(@RequestBody PostDTORequest postDTO) {
        if (postService.createPost(postDTO)) return HttpStatus.OK;
        else return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @GetMapping("/users/{userId}")
    public List<PostDTOResponse> LoadPostByUserHandler(@PathVariable int selectedId, @RequestParam int page,@RequestParam int userId) {
        return postService.loadPostsByUser(page, userId, selectedId);
    }
    @GetMapping("/all")
        public List<PostDTOResponse> LoadPostHandler(@RequestParam int page, @RequestParam(required = false) Integer userId) {
        return postService.loadPosts(page, userId);
    }
    @GetMapping("/post/friends")
    public List<PostDTOResponse> LoadPostByFriendsHandler(@RequestParam int page, @RequestParam int userId) {
        return postService.loadPostsByFriends(page, userId);
    }
}