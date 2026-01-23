package com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostDTORequest {
    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDate published;

    @JsonProperty("visible")
    private boolean visible;

    @JsonProperty("user_id")
    private int userId;

    public PostDTORequest(String content, LocalDate published, boolean visible, int userId) {
        this.content = content;
        this.published = published;
        this.visible = visible;
        this.userId = userId;
    }
}
