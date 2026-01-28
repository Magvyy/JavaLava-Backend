package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class PostDTOResponse {
    private final static DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_name")
    private String username;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private String published;

    @JsonProperty("visible")
    private boolean visible;

    @JsonProperty("like_count")
    private int likeCount;

    @JsonProperty("comment_count")
    private int commentCount;

    public PostDTOResponse(Post post, int likeCount, int commentCount) {
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.userId = post.getUser().getId();
        this.content = post.getContent();
        this.published = post.getPublished().toLocalDateTime().format(customFormatter);
        this.visible = post.isVisible();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public PostDTOResponse(Post post) {
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.userId = post.getUser().getId();
        this.content = post.getContent();
        this.published = post.getPublished().toLocalDateTime().format(customFormatter);
        this.visible = post.isVisible();
    }
}
