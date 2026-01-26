package com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class PostDTOResponse {
    private final static DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private String published;

    @JsonProperty("visible")
    private boolean visible;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("like_count")
    private int likeCount;

    @JsonProperty("comment_count")
    private int commentCount;

    @JsonProperty("post_id")
    private int postId;

    public PostDTOResponse(String content, LocalDateTime published, boolean visible, int userId, String userName,
                           int likeCount, int commentCount, int postId) {
        this.content = content;
        this.published = published.format(customFormatter);
        this.visible = visible;
        this.userId = userId;
        this.userName = userName;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.postId = postId;
    }
}
