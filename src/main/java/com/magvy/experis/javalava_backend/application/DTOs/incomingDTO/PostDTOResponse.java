package com.magvy.experis.javalava_backend.application.DTOs.incomingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class OutgoingPostDTO {
    @JsonProperty("content")
    private String content;

    @JsonProperty("published")
    private LocalDate published;

    @JsonProperty("visible")
    private boolean visible;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("user_name")
    private String userName;

    public OutgoingPostDTO(String content, LocalDate published, boolean visible, int userId, String userName) {
        this.content = content;
        this.published = published;
        this.visible = visible;
        this.userId = userId;
        this.userName = userName;
    }
}
