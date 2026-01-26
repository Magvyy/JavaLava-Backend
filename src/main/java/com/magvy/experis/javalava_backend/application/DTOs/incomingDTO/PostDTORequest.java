package com.magvy.experis.javalava_backend.application.DTOs.incomingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class PostDTORequest {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDateTime published;

    @JsonProperty("visible")
    private boolean visible;

    @JsonProperty("user_id")
    private int userId;

    public PostDTORequest(String content, String published, boolean visible, int userId) {
        this.content = content;
        this.visible = visible;
        this.userId = userId;
        this.published = LocalDateTime.parse(published, formatter);
    }
}
