package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingPostException;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingUserException;
import com.magvy.experis.javalava_backend.domain.exceptions.UnauthorizedActionException;
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

    private Post convertToEntity(PostDTORequest postDTORequest) {
        Optional<User> oUser = userRepository.findById(postDTORequest.getUserId());
        if (oUser.isEmpty()) {
            throw new MissingUserException("User not found");
        }
        return new Post(
                postDTORequest.getId(),
                postDTORequest.getContent(),
                Date.valueOf(postDTORequest.getPublished()),
                postDTORequest.isVisible(),
                oUser.get()
        );
    }

    public Post createPost(User user, PostDTORequest postDTORequest) {
        if (user == null) {
            throw new UnauthorizedActionException("Cannot create a post as an anonymous user.");
        }
        Post post = convertToEntity(postDTORequest);
        return postRepository.save(post);
    }

    public Optional<Post> getPost(User user, int id) {
        if (user == null) {
            return postRepository.findById(id);
        }
        return postRepository.findById(user, id);
    }

    public Post updatePost(User user, PostDTORequest postDTORequest) {
        if (user == null) {
            throw new UnauthorizedActionException("Cannot update a post as an anonymous user.");
        }
        Post post = convertToEntity(postDTORequest);
        return postRepository.save(post);
    }

    public void deletePost(User user, int id) {
        if (user == null) {
            throw new UnauthorizedActionException("Cannot delete a post as an anonymous user.");
        }
        Optional<Post> oPost = postRepository.findById(id);
        if (oPost.isEmpty()) {
            throw new MissingPostException("Can't delete a missing post.");
        }
        Post post = oPost.get();
        if (post.getUser().getId() != user.getId()) {
            throw new UnauthorizedActionException("User does not own this post.");
        }
        postRepository.delete(oPost.get());
    }
}