package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.LikeDTO;
import com.magvy.experis.javalava_backend.domain.entitites.Like;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.entitites.composite.LikeId;
import com.magvy.experis.javalava_backend.domain.exceptions.PostCantAlterLikedException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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

    public ResponseEntity<Like> likePost(LikeDTO likeDTO){
        if (likeRepository.existsByPost_idAndUser_Id(likeDTO.getUserId(), likeDTO.getPostId())){
            throw new PostCantAlterLikedException("Post already liked by user");
        }
        // Lazy loading, user and post only loaded once needed
        System.out.println("1");
        Like newLike = new Like(userRepository.getReferenceById(likeDTO.getUserId()),
                postRepository.getReferenceById(likeDTO.getPostId()));
        System.out.println("2");
        likeRepository.save(newLike);
        System.out.println("3");
        return ResponseEntity.ok(newLike);
    }

    public ResponseEntity<String> unlikePost(LikeDTO likeDTO){
        if (!likeRepository.existsByPost_idAndUser_Id(likeDTO.getPostId(), likeDTO.getUserId())){
            return ResponseEntity.notFound().build();
        }
        Like like = likeRepository.getReferenceById(
                new LikeId(userRepository.getReferenceById(likeDTO.getUserId()),
                        postRepository.getReferenceById(likeDTO.getPostId())));
        likeRepository.delete(like);
        return ResponseEntity.noContent().build();
    }
}
