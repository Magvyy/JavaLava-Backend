package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.magvy.experis.javalava_backend.domain.entitites.User;
import lombok.Getter;

@Getter
public class UserDTOResponse {

    private Long id;
    private String userName;

    public UserDTOResponse() {}

    public UserDTOResponse(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
    }

    public UserDTOResponse(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
