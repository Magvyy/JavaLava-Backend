package com.magvy.experis.javalava_backend.application.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
public class MessageDTO {

    @JsonProperty("message_id")
    private int id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sent")
    private Date date;


    public MessageDTO() {}

    public MessageDTO(int id, String from, String to, String content, Date date)
    {
        this.id = id;
        this.content = content;
        this.date = date;

    }
}
