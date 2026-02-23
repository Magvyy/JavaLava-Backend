package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PermissionsDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PostDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.PostException;
import com.magvy.experis.javalava_backend.domain.util.PostUtil;
import com.magvy.experis.javalava_backend.domain.util.SecurityUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final SecurityUtil securityUtil;
    private final PostUtil postUtil;
    private final int pageSize = 10;

    public PostService(PostRepository postRepository, SecurityUtil securityUtil, PostUtil postUtil) {
        this.postRepository = postRepository;
        this.securityUtil = securityUtil;
        this.postUtil = postUtil;
    }

    public List<PostDTOResponse> loadPosts(int offset) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);

        if (!securityUtil.isAuthenticated()) return postUtil.pageToDTOList(postRepository.findByVisibleTrue(pageable), (long) -1);

        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        Page<Post> posts = postRepository.findPostsForUser(authenticatedUserId, pageable);
        return postUtil.pageToDTOList(posts, authenticatedUserId);
    }

    public List<PostDTOResponse> loadPostsByUser(int offset, Long selectedId) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);

        if (!securityUtil.isAuthenticated()) return postUtil.pageToDTOList(postRepository.findByVisibleTrueAndUserId(selectedId, pageable), (long) -1);

        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        Page<Post> posts = postRepository.findPostsFromUser(authenticatedUserId, selectedId, pageable);
        return postUtil.pageToDTOList(posts, authenticatedUserId);
    }

    public List<PostDTOResponse> loadPostsByFriends(int offset) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        Page<Post> posts = postRepository.findPostsFromFriends(authenticatedUserId, pageable);
        return postUtil.pageToDTOList(posts, authenticatedUserId);
    }

    public PostDTOResponse createPost(PostDTORequest postDTORequest) {
        postUtil.validate(postDTORequest);
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        Post post = postUtil.convertToEntity(postDTORequest, authenticatedUser);
        post = postRepository.save(post);
        return new PostDTOResponse(post);
    }

    public PostDTOResponse readPost(Long id) {
        Post post = postUtil.findByIdOrThrow(id);
        if (!postUtil.isPostVisibleToAuthenticatedUser(post)) throw new PostException("User does not have permission to view this post", HttpStatus.FORBIDDEN);
        return new PostDTOResponse(post);
    }

    public PostDTOResponse updatePost(Long id, PostDTORequest postDTORequest) {
        postUtil.validate(postDTORequest);
        Post post = postUtil.findByIdOrThrow(id);
        if (!postUtil.authenticatedUserOwnsPost(post)) throw new PostException("User does not have permission to edit this post", HttpStatus.FORBIDDEN);
        post = postRepository.save(post);
        return new PostDTOResponse(post);
    }

    public void deletePost(Long id) {
        Post post = postUtil.findByIdOrThrow(id);
        if (!postUtil.authenticatedUserOwnsPost(post) && !securityUtil.authenticatedUserIsAdmin()) throw new PostException("User does not have permission to delete this post", HttpStatus.FORBIDDEN);
        postRepository.delete(post);
    }

    public PermissionsDTOResponse getPermissions(Long id) {
        Post post = postUtil.findByIdOrThrow(id);
        boolean read = postUtil.isPostVisibleToAuthenticatedUser(post);
        boolean write = postUtil.authenticatedUserOwnsPost(post);
        boolean delete = securityUtil.authenticatedUserIsAdmin();
        return new PermissionsDTOResponse(id, read, write, delete);
    }
}
