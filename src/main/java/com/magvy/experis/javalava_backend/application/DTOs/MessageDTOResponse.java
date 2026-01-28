package com.magvy.experis.javalava_backend.application.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import lombok.Getter;
import java.sql.Date;

@Getter
public class MessageDTOResponse {
    @JsonProperty("content")
    private String content;
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
    @JsonProperty("date")
    private Date date;

    public MessageDTOResponse(Message message) {
        this.content = message.getContent();
        this.from = message.getFrom().getUsername();
        this.to = message.getTo().getUsername();
        this.date = message.getDate();
    }

    public MessageDTOResponse() {
    }
}
