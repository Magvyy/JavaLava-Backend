package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.CommentDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.PostDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO.CommentDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Comment;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingUserException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private UserService userService;
    private PostService postService;
    private CommentRepository CommentRepository;
    private final int pageSize = 10;

    public CommentService(UserService userService, PostService postService, CommentRepository commentRepository) {
        this.userService = userService;
        this.postService = postService;
        CommentRepository = commentRepository;
    }


    public boolean createPost(CommentDTORequest commentDTO, User user) {
        if(!postService.isPostVisibleToUser(postService.findByID(commentDTO.getPostId()), user)) {
            throw new MissingUserException("User not authorized to create comments on this post");
        }
        Comment comment = convertToEntity(commentDTO, user);
        CommentRepository.save(comment);
        return true;
    }

    public Comment convertToEntity(CommentDTORequest commentDTO, User user) {
        return new Comment(
                commentDTO.getContent(),
                Date.valueOf(commentDTO.getPublished()),
                postService.findByID(commentDTO.getPostId()),
                user
        );
    }

    public List<CommentDTOResponse> loadCommentsByPost(int page, int postId, User user) {
        Post post = postService.findByID(postId);
        if(!postService.isPostVisibleToUser(postService.findByID(postId), user)) {
            throw new MissingUserException("User not authorized to view comments on this post");
        }
        Sort sort = Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        List<Comment> comments = CommentRepository.findByPost(post, pageable);
        return comments.stream().map(comment -> new CommentDTOResponse(
                comment.getContent(),
                comment.getDate().toLocalDate(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getPost().getId()
        )).toList();
    }
}
