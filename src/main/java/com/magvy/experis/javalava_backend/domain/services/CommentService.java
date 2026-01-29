package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.CommentDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.CommentDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Comment;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingUserException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final UserService userService;
    private final PostService postService;
    private final CommentRepository commentRepository;
    private final int pageSize = 10;

    public CommentService(UserService userService, PostService postService, CommentRepository commentRepository) {
        this.userService = userService;
        this.postService = postService;
        this.commentRepository = commentRepository;
    }


    public ResponseEntity<CommentDTOResponse> createPost(Long postId, User user, CommentDTORequest commentDTO) {
        if(!postService.isPostVisibleToUser(postService.findByID(postId), user)) {
            throw new MissingUserException("User not authorized to create comments on this post");
        }
        Comment comment = convertToEntity(postId, user, commentDTO);
        comment = commentRepository.save(comment);
        CommentDTOResponse commentDTOResponse = new CommentDTOResponse(comment);
        return new ResponseEntity<>(commentDTOResponse, HttpStatus.OK);
    }

    public Comment convertToEntity(Long postId, User user, CommentDTORequest commentDTO) {
        return new Comment(
                commentDTO.getContent(),
                commentDTO.getPublished(),
                postService.findByID(postId),
                user
        );
    }

    public List<CommentDTOResponse> loadCommentsByPost(Long postId, User user, int page) {
        Post post = postService.findByID(postId);
        if(!postService.isPostVisibleToUser(postService.findByID(postId), user)) {
            throw new MissingUserException("User not authorized to view comments on this post");
        }
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        List<Comment> comments = commentRepository.findByPost(post, pageable);
        return comments.stream().map(CommentDTOResponse::new).toList();
    }

    public ResponseEntity<CommentDTOResponse> edit(Long commentId, User user, CommentDTORequest commentDTORequest) {
        Optional<Comment> oComment = commentRepository.findById(commentId);
        if (oComment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Comment comment = oComment.get();
        if (!comment.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        comment = commentRepository.save(convertToEntity(comment.getPost().getId(), user, commentDTORequest));
        CommentDTOResponse commentDTOResponse = new CommentDTOResponse(comment);
        return new ResponseEntity<>(commentDTOResponse, HttpStatus.OK);
    }

    public HttpStatus delete(Long commentId, User user) {
        Optional<Comment> oComment = commentRepository.findById(commentId);
        if (oComment.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        Comment comment = oComment.get();
        if (!comment.getUser().getId().equals(user.getId())) {
            return HttpStatus.UNAUTHORIZED;
        }
        commentRepository.delete(comment);
        return HttpStatus.OK;
    }
}
