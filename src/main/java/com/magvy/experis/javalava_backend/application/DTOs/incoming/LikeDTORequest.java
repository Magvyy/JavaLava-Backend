package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import lombok.Getter;

@Getter
public class LikeDTORequest {
    private Long userId;

    private Long postId;

    public LikeDTORequest(Long userId, Long postId){
        this.userId = userId;
        this.postId = postId;
    }

    public String toString(){
        return String.format("""
            userId: %s,
            postId: %s
            """, this.getUserId(), this.getPostId());
    }
}
