package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConversationDTOResponse {
    private Long id;

    private UserDTOResponse from;

    private UserDTOResponse to;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime sent;

    public ConversationDTOResponse(Message message, User authUser) {
        this.id = (authUser.getId().equals(message.getTo().getId())) ? message.getFrom().getId() : message.getTo().getId();
        this.from = new UserDTOResponse(message.getFrom());
        this.to = new UserDTOResponse(message.getTo());
        this.content = message.getContent();
        this.sent = message.getSent();
    }

    public ConversationDTOResponse() {

    }
}
