package com.magvy.experis.javalava_backend.application.DTOs.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class MessageDTORequest {

    @JsonProperty("message_id")
    private Long id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sent")
    private Date date;

    @JsonProperty("to")
    private Long to;

    public MessageDTORequest() {}

    public MessageDTORequest(Long to, String content)
    {
        this.to = to;
        this.content = content;
    }
}
