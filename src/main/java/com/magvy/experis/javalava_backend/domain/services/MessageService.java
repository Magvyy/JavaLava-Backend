package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
}
