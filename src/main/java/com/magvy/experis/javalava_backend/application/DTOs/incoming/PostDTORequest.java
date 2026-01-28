package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class PostDTORequest {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    @JsonProperty("id")
    private int id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDateTime published;

    @JsonProperty("visible")
    private boolean visible;

    public PostDTORequest(int id, String content, String published, boolean visible) {
        this.id = id;
        this.content = content;
        this.published = LocalDateTime.parse(published, formatter);
        this.visible = visible;
    }
}