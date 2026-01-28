package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class PostDTORequest {
    @JsonProperty("id")
    private int id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDate published;

    @JsonProperty("visible")
    private boolean visible;

    public PostDTORequest(int id, String content, LocalDate date, boolean visible) {
        this.id = id;
        this.content = content;
        this.published = date;
        this.visible = visible;
    }
}