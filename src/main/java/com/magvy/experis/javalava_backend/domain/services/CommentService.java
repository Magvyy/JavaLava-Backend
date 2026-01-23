package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.CommentDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.PostDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Comment;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingUserException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class CommentService {

    private UserService userService;
    private PostService postService;
    private CommentRepository CommentRepository;

    public CommentService(UserService userService, PostService postService, CommentRepository commentRepository) {
        this.userService = userService;
        this.postService = postService;
        CommentRepository = commentRepository;
    }


    public boolean createPost(CommentDTORequest commentDTO) {
        Comment comment = convertToEntity(commentDTO);
        CommentRepository.save(comment);
        return true;
    }

    public Comment convertToEntity(CommentDTORequest commentDTO) {
        return new Comment(
                commentDTO.getContent(),
                Date.valueOf(commentDTO.getPublished()),
                postService.findByID(commentDTO.getPostId()),
                userService.findById(commentDTO.getUserId())
        );
    }
}
