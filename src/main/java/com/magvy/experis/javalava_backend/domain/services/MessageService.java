package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final int pageSize = 10;


    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    private Message ConvertToEntity(MessageDTORequest messageDTORequest, User from) {
        User to = userService.getUserById(messageDTORequest.getTo());
        return new Message(messageDTORequest.getContent(), messageDTORequest.getSent(), from, to);
    }

    public Message sendMessage(MessageDTORequest messageDTORequest, User from) {
        User to = userService.getUserById(messageDTORequest.getTo());

        if (to == null) {
            throw new IllegalArgumentException("Recipient cannot be null");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("Sender and Receiver are the same");
        }
        Message message = ConvertToEntity(messageDTORequest, from);
        return messageRepository.save(message);
    }

    private List<MessageDTOResponse> pageToDTOList(Page<Message> messages) {
        return messages.stream()
                .map(MessageDTOResponse::new)
                .toList();
    }

    public List<MessageDTOResponse> getMessageHistory(User to, Long from_id, int page) {
        Sort sort = Sort.by("sent").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        User from = userService.getUserById(from_id);
        Page<Message> messagePage = messageRepository.findByToAndFrom(to, from, pageable);
        return pageToDTOList(messagePage);
    }

}
