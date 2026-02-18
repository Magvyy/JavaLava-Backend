package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorDTOResponse {
    private int status;
    private String message;

    public ErrorDTOResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorDTOResponse() {

    }
}
