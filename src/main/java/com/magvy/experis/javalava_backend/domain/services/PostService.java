package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.PostDTO;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingUserException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import com.magvy.experis.javalava_backend.infrastructure.readonly.ReadOnlyUserRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ReadOnlyUserRepository userRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository, ReadOnlyUserRepository userRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    private Post convertToEntity(PostDTO postDTO) {
        Optional<User> oUser = userRepository.findById(postDTO.getUserId());
        if (oUser.isEmpty()) {
            throw new MissingUserException("User not found");
        }
        return new Post(
                postDTO.getContent(),
                Date.valueOf(postDTO.getPublished()),
                postDTO.isVisible(),
                oUser.get()
        );
    }

    public boolean createPost(PostDTO postDTO) {
        Post post = convertToEntity(postDTO);
        postRepository.save(post);
        return true;
    }
}
