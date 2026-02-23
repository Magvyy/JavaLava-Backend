package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.domain.entitites.Like;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.entitites.composite.LikeId;
import com.magvy.experis.javalava_backend.domain.exceptions.LikeException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class LikeUtil {
    private final LikeRepository likeRepository;
    private final SecurityUtil securityUtil;
    private final PostUtil postUtil;

    public LikeUtil(LikeRepository likeRepository, SecurityUtil securityUtil, PostUtil postUtil) {
        this.likeRepository = likeRepository;
        this.securityUtil = securityUtil;
        this.postUtil = postUtil;
    }

    public Like convertToEntity(Long postId) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        Post post = postUtil.findByIdOrThrow(postId);
        return new Like(
                authenticatedUser,
                post
        );
    }

    public LikeId createLikeId(Long postId) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        Post post = postUtil.findByIdOrThrow(postId);
        return new LikeId(authenticatedUser, post);
    }

    public Like findByIdOrThrow(LikeId id) {
        return likeRepository.findById(id).orElseThrow(() -> new LikeException("Like not found", HttpStatus.NOT_FOUND));
    }
}
