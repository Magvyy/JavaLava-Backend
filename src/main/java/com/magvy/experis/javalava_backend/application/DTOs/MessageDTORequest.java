package com.magvy.experis.javalava_backend.application.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import lombok.Getter;

import java.sql.Date;
@Getter
public class MessageDTORequest {
    @JsonProperty("content")
    private String content;
    @JsonProperty("from")
    private String sender;
    @JsonProperty("to")
    private String receiver;
    @JsonProperty("date")
    private Date date;

    public MessageDTORequest(Message message) {
        this.content = message.getContent();
        this.sender = message.getFrom().getUsername();
        this.receiver = message.getTo().getUsername();
        this.date = message.getDate();
    }

    public MessageDTORequest() {
    }
}
