package com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AuthDTO {
    @JsonProperty("user_name")
    private String username;
    private String password;

    public AuthDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toString() {
        return String.format(
            """
            Username: %s
            Password: %s
            """, this.username, this.password);
    }
}
