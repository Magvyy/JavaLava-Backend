package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.LikeDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Like;
import com.magvy.experis.javalava_backend.domain.entitites.composite.LikeId;
import com.magvy.experis.javalava_backend.domain.exceptions.LikeException;
import com.magvy.experis.javalava_backend.domain.util.LikeUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeUtil likeUtil;
    private final WebSocketService webSocketService;


    @Autowired
    public LikeService(LikeRepository likeRepository, LikeUtil likeUtil, WebSocketService webSocketService){
        this.likeRepository = likeRepository;
        this.likeUtil = likeUtil;
        this.webSocketService = webSocketService;
    }

    public void likePost(LikeDTORequest likeDTORequest) {
        LikeId likeId = likeUtil.createLikeId(likeDTORequest);
        if (likeRepository.existsById(likeId)) throw new LikeException("User has already liked this post", HttpStatus.CONFLICT);
        // Lazy loading, user and post only loaded once needed
        Like like = likeUtil.convertToEntity(likeDTORequest);
        like = likeRepository.save(like);
        webSocketService.sendNotification(
                like.getPost().getUser().getUserName(),
                "Your post has a new like from " + like.getUser().getUserName()
        );
    }

    public void unlikePost(LikeDTORequest likeDTORequest){
        LikeId likeId = likeUtil.createLikeId(likeDTORequest);
        if (!likeRepository.existsById(likeId)) throw new LikeException("User has not liked this post", HttpStatus.BAD_REQUEST);
        Like like = likeUtil.convertToEntity(likeDTORequest);
        likeRepository.delete(like);
    }
}