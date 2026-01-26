package com.magvy.experis.javalava_backend.application.DTOs.incomingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PostDTORequest {
    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDateTime published;

    @JsonProperty("visible")
    private boolean visible;

    @JsonProperty("user_id")
    private int userId;

    public PostDTORequest(String content, LocalDateTime published, boolean visible, int userId) {
        this.content = content;
        this.published = published;
        this.visible = visible;
        this.userId = userId;
    }
}
