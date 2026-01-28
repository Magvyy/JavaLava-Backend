package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentDTOResponse {
    private final static DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @JsonProperty("id")
    private Long id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private String published;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("post_id")
    private Long postId;

    public CommentDTOResponse(Long id, String content, LocalDateTime published, Long userId, String userName, Long postId) {
        this.id = id;
        this.content = content;
        this.published = published.format(customFormatter);
        this.userId = userId;
        this.userName = userName;
        this.postId = postId;
    }
}