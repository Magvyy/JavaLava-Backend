package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import lombok.Getter;

@Getter
public class PermissionsDTOResponse {
    private Long id;
    private boolean read;
    private boolean write;
    private boolean delete;

    public PermissionsDTOResponse(Long id, boolean read, boolean write, boolean delete) {
        this.id = id;
        this.read = read;
        this.write = write;
        this.delete = delete;
    }
}
