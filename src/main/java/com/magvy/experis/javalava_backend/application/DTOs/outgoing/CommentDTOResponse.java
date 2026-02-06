package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magvy.experis.javalava_backend.domain.entitites.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDTOResponse {
    private Long id;

    private UserDTOResponse user;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime published;

    private Long postId;

    public CommentDTOResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.published = comment.getPublished().toLocalDateTime();
        this.user = new UserDTOResponse(comment.getUser());
        this.postId = comment.getPost().getId();
    }
}