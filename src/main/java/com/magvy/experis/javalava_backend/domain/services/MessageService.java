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

    private Message ConvertToEntity(MessageDTORequest messageDTORequest, User sender) {
        User recipient = userService.getUserById(messageDTORequest.getToUserId());
        return new Message(messageDTORequest.getContent(), messageDTORequest.getSent(), sender, recipient);
    }

    public Message sendMessage(MessageDTORequest messageDTORequest, User sender) {
        User recipient = userService.getUserById(messageDTORequest.getToUserId());

        if (recipient == null) {
            throw new IllegalArgumentException("Recipient cannot be null");
        }

        if (sender.equals(recipient)) {
            throw new IllegalArgumentException("Sender and recipient are the same");
        }
        Message message = ConvertToEntity(messageDTORequest, sender);
        return messageRepository.save(message);
    }

    public List<MessageDTOResponse> getConversations(User recipient, int page) {
        Sort sort = Sort.by("sent").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        List<Message> messageList = messageRepository.getConversations(recipient.getId());
        List<MessageDTOResponse> messageDTOResponses = new ArrayList<>();
        for (Message message : messageList) {
            messageDTOResponses.add(new MessageDTOResponse(message));
        }
        return messageDTOResponses;
    }

    public List<MessageDTOResponse> getConversation(User recipient, Long sender_id, int page) {
        Sort sort = Sort.by("sent").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        User sender = userService.getUserById(sender_id);
        Page<Message> messagePage = messageRepository.findByToAndFrom(recipient, sender, pageable);
        List<Message> messageList = messageRepository.findByFromAndToOrFromAndTo(recipient, sender, sender, recipient);
        return pageToDTOList(messagePage);
    }

    private List<MessageDTOResponse> pageToDTOList(Page<Message> messages) {
        return messages.stream()
                .map(MessageDTOResponse::new)
                .toList();
    }


}
