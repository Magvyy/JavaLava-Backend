package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.AuthDTO;
import com.magvy.experis.javalava_backend.application.DTOs.LikeDTO;
import com.magvy.experis.javalava_backend.application.DTOs.PostDTO;
import com.magvy.experis.javalava_backend.application.security.util.JwtUtil;
import com.magvy.experis.javalava_backend.domain.entitites.Like;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.services.LikeService;
import com.magvy.experis.javalava_backend.domain.services.PostService;
import com.magvy.experis.javalava_backend.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final LikeService likeService;

    @Autowired
    public PostController(PostService postService, LikeService likeService) {
        this.postService = postService;
        this.likeService = likeService;
    }

    @PostMapping("/create")
    public HttpStatus LoginPostHandler(@RequestBody PostDTO postDTO) {
        if (postService.createPost(postDTO)) return HttpStatus.OK;
        else return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @PutMapping("/like")
    public ResponseEntity<Like> LikePutHandler(@RequestBody LikeDTO likeDTO){
        return likeService.likePost(likeDTO);
    }
    @DeleteMapping("/unlike")
    public ResponseEntity<String> LikeDeleteHandler(@RequestBody LikeDTO likeDTO){
        return likeService.unlikePost(likeDTO);
    }
}