package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class MessageDTORequest {

    @JsonProperty("message_id")
    private int id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sent")
    private Date date;

    @JsonProperty("to")
    private int to;

    public MessageDTORequest() {}

    public MessageDTORequest(int to, String content)
    {
        this.to = to;
        this.content = content;
    }
}
