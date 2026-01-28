package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostDTOResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("user_name")
    private String username;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDate published;

    @JsonProperty("visible")
    private boolean visible;

    public PostDTOResponse(Post post) {
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.userId = post.getUser().getId();
        this.content = post.getContent();
        this.published = LocalDate.parse(post.getPublished().toString());
        this.visible = post.isVisible();
    }
}
