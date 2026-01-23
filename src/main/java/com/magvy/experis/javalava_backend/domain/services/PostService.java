package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO.PostDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.PostDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingUserException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import com.magvy.experis.javalava_backend.infrastructure.readonly.ReadOnlyUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ReadOnlyUserRepository userRepository;
    private final int pageSize = 10;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository, ReadOnlyUserRepository userRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    private Post convertToEntity(PostDTORequest postDTO) {
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

    public boolean createPost(PostDTORequest postDTO) {
        Post post = convertToEntity(postDTO);
        postRepository.save(post);
        return true;
    }

    public List<PostDTOResponse> loadPosts(int page, Integer userId) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        if (userId == null) {
            return pageToDTOList(postRepository.findByVisibleTrue(pageable));
        }
        return pageToDTOList(postRepository.findPostsForUser(userId, pageable));
    }

    public List<PostDTOResponse> loadPostsByUser(int page, int userId, int selectedId) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return pageToDTOList(postRepository.findPostsFromUser(userId, selectedId, pageable));

    }
    public List<PostDTOResponse> loadPostsByFriends(int page, int userId) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return pageToDTOList(postRepository.findPostsFromFriends(userId, pageable));
    }

    private List<PostDTOResponse> pageToDTOList(Page<Post> posts) {
        return posts.stream()
                .map(p -> new PostDTOResponse(
                        p.getContent(),
                        p.getPublished().toLocalDate(),
                        p.isVisible(),
                        p.getUser().getId(),
                        p.getUser().getUsername(),
                        (int)likeRepository.countByPost(p),
                        (int)commentRepository.countByPost(p),
                        p.getId()
                ))
                .toList();
    }
    public Post findByID(int postId) {
        return postRepository.findById(postId).orElseThrow(() -> new MissingResourceException("Post not found", "Post", String.valueOf(postId)));
    }
}
