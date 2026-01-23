package com.magvy.experis.javalava_backend.application.DTOs.incomingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentDTORequest {
    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDate published;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("post_id")
    private int postId;

    public CommentDTORequest(String content, LocalDate published, int userId, int postId) {
        this.content = content;
        this.published = published;
        this.userId = userId;
        this.postId = postId;
    }
}
