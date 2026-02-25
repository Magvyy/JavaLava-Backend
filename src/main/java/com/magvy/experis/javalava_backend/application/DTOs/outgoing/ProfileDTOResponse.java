package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.enums.FriendStatus;
import lombok.Getter;

@Getter
public class ProfileDTOResponse {

    private Long id;
    private String userName;
    private FriendStatus friendStatus;
    private AttachmentDTO attachment;

    public ProfileDTOResponse() {}

    public ProfileDTOResponse(User user, FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
        this.id = user.getId();
        this.userName = user.getUserName();
        this.attachment = (user.getAttachment() != null) ? new AttachmentDTO(user.getAttachment()) : null;
    }
}

