package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDTOResponse {
    private Long id;

    private String userName;

    private Long userId;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime published;

    private boolean visible;

    private int likeCount;

    private int commentCount;

    public PostDTOResponse(Post post, int likeCount, int commentCount) {
        this.id = post.getId();
        this.userName = post.getUser().getUserName();
        this.userId = post.getUser().getId();
        this.content = post.getContent();
        this.published = post.getPublished().toLocalDateTime();
        this.visible = post.isVisible();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public PostDTOResponse(Post post) {
        this.id = post.getId();
        this.userName = post.getUser().getUserName();
        this.userId = post.getUser().getId();
        this.content = post.getContent();
        this.published = post.getPublished().toLocalDateTime();
        this.visible = post.isVisible();
    }
}
