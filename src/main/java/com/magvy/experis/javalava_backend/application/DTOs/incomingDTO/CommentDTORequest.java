package com.magvy.experis.javalava_backend.application.DTOs.incomingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CommentDTORequest {
    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDateTime published;

    @JsonProperty("post_id")
    private int postId;

    public CommentDTORequest(String content, LocalDateTime published, int postId) {
        this.content = content;
        this.published = published;
        this.postId = postId;
    }
}
