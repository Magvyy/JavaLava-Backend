package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LikeDTO {
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("post_id")
    private int postId;

    public LikeDTO(int userId, int postId){
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
