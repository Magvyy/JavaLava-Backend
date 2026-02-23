package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.security.config.custom.CustomUserDetails;
import com.magvy.experis.javalava_backend.controllers.util.ResponseUtil;
import com.magvy.experis.javalava_backend.domain.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController extends BaseAuthController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like/post/{postId}")
    public ResponseEntity<Void> LikePutHandler(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails principal){
        throwIfUserNull(principal);
        likeService.likePost(postId);
        return ResponseUtil.wrapEntity(null);
    }
    @DeleteMapping("/unlike/post/{postId}")
    public ResponseEntity<Void> LikeDeleteHandler(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails principal){
        throwIfUserNull(principal);
        likeService.unlikePost(postId);
        return ResponseUtil.wrapEntity(null);
    }
}
