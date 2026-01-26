package com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentDTOResponse {
    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDate published;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("post_id")
    private int postId;

    public CommentDTOResponse(String content, LocalDate published, int userId, String userName, int postId) {
        this.content = content;
        this.published = published;
        this.userId = userId;
        this.userName = userName;
        this.postId = postId;
    }
}