package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PermissionsDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PostDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.PostException;
import com.magvy.experis.javalava_backend.domain.exceptions.UserException;
import com.magvy.experis.javalava_backend.domain.exceptions.UnauthorizedActionException;
import com.magvy.experis.javalava_backend.domain.util.PostUtil;
import com.magvy.experis.javalava_backend.domain.util.SecurityUtil;
import com.magvy.experis.javalava_backend.domain.util.UserUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final SecurityUtil securityUtil;
    private final PostRepository postRepository;
    private final PostUtil postUtil;
    private final UserUtil userUtil;
    private final int pageSize = 10;

    public PostService(SecurityUtil securityUtil, PostRepository postRepository, PostUtil postUtil, UserUtil userUtil) {
        this.securityUtil = securityUtil;
        this.postRepository = postRepository;
        this.postUtil = postUtil;
        this.userUtil = userUtil;
    }

    public List<PostDTOResponse> loadPosts(int offset) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);

        if (!securityUtil.isAuthenticated()) return postUtil.pageToDTOList(postRepository.findByVisibleTrue(pageable), (long) -1);

        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        return postUtil.pageToDTOList(postRepository.findPostsForUser(authenticatedUserId, pageable), authenticatedUserId);
    }

    public List<PostDTOResponse> loadPostsByUser(int offset, Long selectedId) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);

        if (!securityUtil.isAuthenticated()) return postUtil.pageToDTOList(postRepository.findByVisibleTrueAndUserId(selectedId, pageable), (long) -1);

        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        return postUtil.pageToDTOList(postRepository.findPostsFromUser(authenticatedUserId, selectedId, pageable), authenticatedUserId);
    }

    public List<PostDTOResponse> loadPostsByFriends(int offset) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        return postUtil.pageToDTOList(postRepository.findPostsFromFriends(authenticatedUserId, pageable), authenticatedUserId);
    }

    public Post createPost(PostDTORequest postDTORequest) {
        if (!postUtil.isValidContent(postDTORequest.getContent())) throw new IllegalArgumentException("Content must be something.");
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        Post post = postUtil.convertToEntity(postDTORequest, authenticatedUser);
        return postRepository.save(post);
    }

    public Optional<Post> getPost(Long id) {
        if (!securityUtil.isAuthenticated()) return postRepository.findByIdAndVisibleTrue(id);
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        return postRepository.findByIdIfUserLoggedIn(authenticatedUser, id);
    }

    public Post updatePost(Long id, PostDTORequest postDTORequest) {
        if (!postUtil.isValidContent(postDTORequest.getContent())) throw new PostException("Content is invalid", HttpStatus.BAD_REQUEST);
        Post post = postUtil.findByIdOrThrow(id);
        if (!postUtil.authenticatedUserOwnsPost(post)) throw new UnauthorizedActionException("User does not own this post.");
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = postUtil.findByIdOrThrow(id);
        if (!postUtil.authenticatedUserOwnsPost(post) && !securityUtil.authenticatedUserIsAdmin()) throw new UnauthorizedActionException("User does not own this post.");
        postRepository.delete(post);
    }

    public PermissionsDTOResponse getPermissions(Long id, User user) {
        Post post = postUtil.findByIdOrThrow(id);
        boolean read = postUtil.isPostVisibleToUser(post, user);
        boolean write = post.getUser().getId().equals(user.getId());
        boolean delete = userUtil.isAdmin(user.getId());
        return new PermissionsDTOResponse(id, read, write, delete);
    }
}
