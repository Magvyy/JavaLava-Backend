package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.LikeDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Like;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    @Autowired
    public LikeService(LikeRepository likeRepository,
                       UserRepository userRepository,
                       PostRepository postRepository){
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    private Like convertToEntity(LikeDTORequest likeDTORequest) {
        return new Like(userRepository.getReferenceById(likeDTORequest.getUserId()),
                postRepository.getReferenceById(likeDTORequest.getPostId()));
    }

    public ResponseEntity<Like> likePost(LikeDTORequest likeDTORequest){
        if (likeRepository.existsByPost_idAndUser_Id(likeDTORequest.getUserId(), likeDTORequest.getPostId())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // Lazy loading, user and post only loaded once needed
        Like newLike = convertToEntity(likeDTORequest);
        likeRepository.save(newLike);
        return ResponseEntity.ok(newLike);
    }

    public ResponseEntity<String> unlikePost(LikeDTORequest likeDTORequest){
        if (!likeRepository.existsByPost_idAndUser_Id(likeDTORequest.getPostId(), likeDTORequest.getUserId())){
            return ResponseEntity.notFound().build();
        }
        Like like = convertToEntity(likeDTORequest);
        likeRepository.delete(like);
        return ResponseEntity.noContent().build();
    }
}
