package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.enums.FriendStatus;
import lombok.Getter;

@Getter
public class ProfileDTOResponse {

    private Long id;
    private String userName;
    private FriendStatus friendStatus;

    public ProfileDTOResponse() {}

    public ProfileDTOResponse(User user, FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
        this.id = user.getId();
        this.userName = user.getUserName();
    }
}

