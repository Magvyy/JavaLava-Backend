package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LikeDTORequest {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("post_id")
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
