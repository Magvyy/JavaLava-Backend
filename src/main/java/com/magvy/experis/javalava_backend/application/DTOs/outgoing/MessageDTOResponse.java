package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDTOResponse {
    private Long id;

    private UserDTOResponse from;

    private UserDTOResponse to;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime sent;

    public MessageDTOResponse(Message message) {
        this.id = message.getId();
        this.from = new UserDTOResponse(message.getFrom());
        this.to = new UserDTOResponse(message.getTo());
        this.content = message.getContent();
        this.sent = message.getSent();
    }

    public MessageDTOResponse() {

    }
}
