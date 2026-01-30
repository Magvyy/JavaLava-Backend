package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.LikeDTORequest;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController extends BaseAuthHController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like/post/{postId}")
    public ResponseEntity<String> LikePutHandler(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails principal){
        Long userId = throwIfUserNull(principal).getId();
        LikeDTORequest likeDTORequest = new LikeDTORequest(userId, postId);
        return likeService.likePost(likeDTORequest);
    }
    @DeleteMapping("/unlike/post/{postId}")
    public ResponseEntity<String> LikeDeleteHandler(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails principal){
        Long userId = throwIfUserNull(principal).getId();
        LikeDTORequest likeDTORequest = new LikeDTORequest(userId, postId);
        return likeService.unlikePost(likeDTORequest);
    }
}
