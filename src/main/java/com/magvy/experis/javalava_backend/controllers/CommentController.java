package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.CommentDTORequest;
import com.magvy.experis.javalava_backend.domain.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public HttpStatus CreateCommentHandler(@RequestBody CommentDTORequest commentDTO) {
        if (commentService.createPost(commentDTO)) return HttpStatus.OK;
        else return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
