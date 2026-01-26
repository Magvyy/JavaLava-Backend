package com.magvy.experis.javalava_backend.application.DTOs.incomingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentDTORequest {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDateTime published;

    @JsonProperty("post_id")
    private int postId;

    public CommentDTORequest(String content, String published, int postId) {
        this.content = content;
        this.published = LocalDateTime.parse(published, formatter);
        this.postId = postId;
    }
}
