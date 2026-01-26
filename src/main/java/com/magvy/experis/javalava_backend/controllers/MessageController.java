package com.magvy.experis.javalava_backend.controllers;
import com.magvy.experis.javalava_backend.application.DTOs.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.MessageDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.MessageService;
import com.magvy.experis.javalava_backend.infrastructure.readonly.ReadOnlyUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final ReadOnlyUserRepository userRepository;


    public MessageController(MessageService messageService, ReadOnlyUserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @PostMapping()
    public ResponseEntity<MessageDTOResponse> sendMessage(@RequestBody MessageDTORequest messageDTORequest, Authentication authentication) {
        User user = getLoggedInUser(authentication);
        Message message = messageService.sendMessage(messageDTORequest, user);
        return new ResponseEntity<>(new MessageDTOResponse(message), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<MessageDTOResponse>> readMessage(@PathVariable int sender_id, Authentication authentication) {
        User user = getLoggedInUser(authentication);
        List<MessageDTOResponse> messageHistory = messageService.getMessageHistory(user, sender_id);

        return new ResponseEntity<>(messageHistory, HttpStatus.OK);
    }

    private User getLoggedInUser(Authentication auth) {
        Object principal = auth.getPrincipal();
        if (principal instanceof String username) {
            Optional<User> oUser = userRepository.findByUsername(username);
            return oUser.orElse(null);
        }
        return null;
    }

}
