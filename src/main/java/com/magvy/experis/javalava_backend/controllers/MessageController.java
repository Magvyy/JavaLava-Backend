package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.MessageService;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String from, @RequestParam String to, @RequestBody String content){
        System.out.println("From: " + from);
        return messageService.sendMessage(to, from, content);
    }

    @GetMapping("/read")
    public String readMessage(@RequestParam String from, @RequestParam String to) {
        System.out.println("read: " + from);

        return messageService.readNewestMessage(from, to);
    }


}
