package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class PostDTORequest {
    @JsonProperty("id")
    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDate published;

    @JsonProperty("visible")
    private boolean visible;

    public PostDTORequest(int id, int userId, String content, LocalDate date, boolean visible) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.published = date;
        this.visible = visible;
    }
}