package com.magvy.experis.javalava_backend.application.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import lombok.Getter;

import java.sql.Date;

@Getter
public class MessageDTORequest {

    @JsonProperty("message_id")
    private int id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sent")
    private Date date;

    @JsonProperty("To")
    private int receiver_id;

    public MessageDTORequest() {}

    public MessageDTORequest(int receiver_id, String content, Date date)
    {
        this.receiver_id = receiver_id;
        this.content = content;
        this.date = date;

    }
}
