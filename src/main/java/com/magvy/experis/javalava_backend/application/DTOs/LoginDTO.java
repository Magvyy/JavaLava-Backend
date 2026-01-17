package com.magvy.experis.javalava_backend.application.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDTO {
    @JsonProperty("user_name")
    private String username;
    private String password;

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return String.format(
            """
            Username: %s
            Password: %s
            """, this.username, this.password);
    }
}
