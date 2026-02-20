package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import lombok.Getter;

@Getter
public class PostDTORequest {
    private String content;

    private boolean visible;
}