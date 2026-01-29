package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import lombok.Getter;

@Getter
public class AuthDTO {
    private String userName;
    private String password;

    public AuthDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String toString() {
        return String.format(
            """
            Username: %s
            Password: %s
            """, this.userName, this.password);
    }
}
