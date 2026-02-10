package com.magvy.experis.javalava_backend.domain.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendMessage(String username, String message) {
        simpMessagingTemplate.convertAndSendToUser(username,"/queue", message);
    }

    public void sendNotification(String username, String notification) {
        simpMessagingTemplate.convertAndSendToUser(username,"/queue/notifications", notification);
    }
}
