package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDTOResponse {
    private Long id;

    private UserDTOResponse user;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime published;

    private boolean visible;

    private boolean liked;

    private int likeCount;

    private int commentCount;

    public PostDTOResponse(Post post, int likeCount, int commentCount) {
        this.id = post.getId();
        this.user = new UserDTOResponse(post.getUser());
        this.content = post.getContent();
        this.published = post.getPublished().toLocalDateTime();
        this.visible = post.isVisible();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public PostDTOResponse(Post post, boolean liked, int likeCount, int commentCount) {
        this.id = post.getId();
        this.user = new UserDTOResponse(post.getUser());
        this.content = post.getContent();
        this.published = post.getPublished().toLocalDateTime();
        this.visible = post.isVisible();
        this.liked = liked;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public PostDTOResponse(Post post) {
        this.id = post.getId();
        this.user = new UserDTOResponse(post.getUser());
        this.content = post.getContent();
        this.published = post.getPublished().toLocalDateTime();
        this.visible = post.isVisible();
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
