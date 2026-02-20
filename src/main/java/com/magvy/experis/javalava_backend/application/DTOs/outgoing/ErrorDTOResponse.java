package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import lombok.Getter;


@Getter
public class ErrorDTOResponse {
    private int status;
    private String message;
    private String endpoint;

    public ErrorDTOResponse(int status, String message, String endpoint) {
        this.status = status;
        this.message = message;
        this.endpoint = endpoint;
    }

    public ErrorDTOResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorDTOResponse() {

    }
}
