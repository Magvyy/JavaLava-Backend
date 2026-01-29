package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.magvy.experis.javalava_backend.domain.entitites.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentDTOResponse {
    private Long id;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime published;

    private Long userId;

    private String userName;

    private Long postId;

    public CommentDTOResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.published = comment.getPublished().toLocalDateTime();
        this.userId = comment.getUser().getId();
        this.userName = comment.getUser().getUserName();
        this.postId = comment.getPost().getId();
    }
}