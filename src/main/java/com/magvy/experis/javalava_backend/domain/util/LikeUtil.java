package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.LikeDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Comment;
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
    private final UserUtil userUtil;
    private final PostUtil postUtil;

    public LikeUtil(LikeRepository likeRepository, SecurityUtil securityUtil, UserUtil userUtil, PostUtil postUtil) {
        this.likeRepository = likeRepository;
        this.securityUtil = securityUtil;
        this.userUtil = userUtil;
        this.postUtil = postUtil;
    }

    public Like convertToEntity(LikeDTORequest likeDTORequest) {
        User user = userUtil.findByIdOrThrow(likeDTORequest.getUserId());
        Post post = postUtil.findByIdOrThrow(likeDTORequest.getPostId());
        return new Like(
                user,
                post
        );
    }

    public LikeId createLikeId(LikeDTORequest likeDTORequest) {
        User user = userUtil.findByIdOrThrow(likeDTORequest.getUserId());
        Post post = postUtil.findByIdOrThrow(likeDTORequest.getPostId());
        return new LikeId(user, post);
    }

    public Like findByIdOrThrow(LikeId id) {
        return likeRepository.findById(id).orElseThrow(() -> new LikeException("Like not found", HttpStatus.NOT_FOUND));
    }

    public boolean authenticatedUserLikesPost(LikeId id) {

    }
}
