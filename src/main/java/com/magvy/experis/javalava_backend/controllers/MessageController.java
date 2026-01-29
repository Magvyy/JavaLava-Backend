package com.magvy.experis.javalava_backend.controllers;
import com.magvy.experis.javalava_backend.application.DTOs.incoming.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseAuthHController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping()
    public ResponseEntity<MessageDTOResponse> sendMessage(@RequestBody MessageDTORequest messageDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        Message message = messageService.sendMessage(messageDTORequest, user);
        return new ResponseEntity<>(new MessageDTOResponse(message), HttpStatus.OK);
    }

    @GetMapping("/{sender_id}")
    public ResponseEntity<List<MessageDTOResponse>> readMessage(@PathVariable Long sender_id, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        List<MessageDTOResponse> messageHistory = messageService.getMessageHistory(user, sender_id);

        return new ResponseEntity<>(messageHistory, HttpStatus.OK);
    }

}
