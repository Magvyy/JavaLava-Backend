package com.magvy.experis.javalava_backend.domain.services;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendMessage(String username, Message message) {
        simpMessagingTemplate.convertAndSendToUser(username,"/queue/messages", new MessageDTOResponse(message));
    }

    public void sendNotification(String username, String notification) {
        simpMessagingTemplate.convertAndSendToUser(username,"/queue/notifications", notification);
    }
}
