package com.magvy.experis.javalava_backend.controllers;
import com.magvy.experis.javalava_backend.application.DTOs.incoming.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.MessageService;
import com.magvy.experis.javalava_backend.domain.services.WebSocketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController extends BaseAuthHController {

    private final MessageService messageService;
    private final WebSocketService websocketService;

    public MessageController(MessageService messageService, WebSocketService websocketService) {
        this.messageService = messageService;
        this.websocketService = websocketService;
    }

    @PostMapping()
    public ResponseEntity<MessageDTOResponse> sendMessage(@RequestBody MessageDTORequest messageDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        Message message = messageService.sendMessage(messageDTORequest, user);
        websocketService.sendMessage(message.getTo().getUserName(), message.getContent());
        return new ResponseEntity<>(new MessageDTOResponse(message), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<MessageDTOResponse>> getConversations(@RequestParam(value = "page") int page, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        List<MessageDTOResponse> messages = messageService.getConversations(user, page);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/{sender_id}")
    public ResponseEntity<List<MessageDTOResponse>> getConversation(@PathVariable Long sender_id, @RequestParam(value = "page") int page, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        List<MessageDTOResponse> conversation = messageService.getConversation(user, sender_id, page);
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }
}
