package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDTORequest {
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime sent;

    private Long from;

    private Long to;
}