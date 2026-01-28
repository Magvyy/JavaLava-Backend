package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.LikeDTO;
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

    private Like convertToEntity(LikeDTO likeDTO) {
        return new Like(userRepository.getReferenceById(likeDTO.getUserId()),
                postRepository.getReferenceById(likeDTO.getPostId()));
    }

    public ResponseEntity<Like> likePost(LikeDTO likeDTO){
        if (likeRepository.existsByPost_idAndUser_Id(likeDTO.getUserId(), likeDTO.getPostId())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // Lazy loading, user and post only loaded once needed
        Like newLike = convertToEntity(likeDTO);
        likeRepository.save(newLike);
        return ResponseEntity.ok(newLike);
    }

    public ResponseEntity<String> unlikePost(LikeDTO likeDTO){
        if (!likeRepository.existsByPost_idAndUser_Id(likeDTO.getPostId(), likeDTO.getUserId())){
            return ResponseEntity.notFound().build();
        }
        Like like = convertToEntity(likeDTO);
        likeRepository.delete(like);
        return ResponseEntity.noContent().build();
    }
}
