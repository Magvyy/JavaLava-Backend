package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDTOResponse {
    private Long id;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime sent;

    private String from;

    private String to;

    private Long fromId;

    private Long toId;

    public MessageDTOResponse(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.sent = message.getSent();
        this.from = message.getFrom().getUserName();
        this.to = message.getTo().getUserName();
        this.fromId = message.getFrom().getId();
        this.toId = message.getTo().getId();
    }

    public MessageDTOResponse() {

    }
}
