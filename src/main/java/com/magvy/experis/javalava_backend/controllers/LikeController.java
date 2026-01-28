package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.LikeDTO;
import com.magvy.experis.javalava_backend.domain.entitites.Like;
import com.magvy.experis.javalava_backend.domain.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
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
