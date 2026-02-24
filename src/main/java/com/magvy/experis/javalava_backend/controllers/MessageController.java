package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ConversationDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.custom.CustomUserDetails;
import com.magvy.experis.javalava_backend.controllers.util.ResponseUtil;
import com.magvy.experis.javalava_backend.domain.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController extends BaseAuthController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<MessageDTOResponse> sendMessage(@PathVariable Long id, @RequestBody MessageDTORequest messageDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        MessageDTOResponse message = messageService.sendMessage(messageDTORequest, id);
        return ResponseUtil.wrapEntity(message);
    }

    @GetMapping()
    public ResponseEntity<List<ConversationDTOResponse>> getConversations(@RequestParam(value = "offset") int offset, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        List<ConversationDTOResponse> messages = messageService.getConversations(offset);
        return ResponseUtil.wrapEntity(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<MessageDTOResponse>> getConversation(@PathVariable Long id, @RequestParam(value = "offset") int offset, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        List<MessageDTOResponse> conversation = messageService.getConversation(id, offset);
        return ResponseUtil.wrapEntity(conversation);
    }
}
