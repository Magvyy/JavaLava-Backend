package com.magvy.experis.javalava_backend.application.DTOs.incomingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostDTOResponse {
    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDate published;

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

    public PostDTOResponse(String content, LocalDate published, boolean visible, int userId, String userName,
                           int likeCount, int commentCount, int postId) {
        this.content = content;
        this.published = published;
        this.visible = visible;
        this.userId = userId;
        this.userName = userName;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.postId = postId;
    }
}
