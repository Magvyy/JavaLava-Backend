package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;


    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    private Message ConvertToEntity(MessageDTORequest messageDTORequest, User sender) {
        User receiver = userService.getUserById(messageDTORequest.getToUserId());
        return new Message(messageDTORequest.getContent(), messageDTORequest.getSent(), sender, receiver);
    }

    public Message sendMessage(MessageDTORequest messageDTORequest, User sender) {
        User recipient = userService.getUserById(messageDTORequest.getToUserId());

//       if (!friendService.isFriend(recipient, sender)) { TODO IMPLEMENT Friends
//          throw new IllegalArgumentException("Can only send message to a friend");
//       }

        if (recipient == null) {
            throw new IllegalArgumentException("Recipient cannot be null");
        }
        if (sender == null) {
            throw new IllegalArgumentException("Sender cannot be null");
        }
        if (sender.equals(recipient)) {
            throw new IllegalArgumentException("Sender and Receiver are the same");
        }
        Message message = ConvertToEntity(messageDTORequest, sender);
        return messageRepository.save(message);
    }

    public List<MessageDTOResponse> getConversation(User receiver, Long sender_id) {
        User sender = userService.getUserById(sender_id);
        List<Message> messageList = messageRepository.findByFromAndToOrFromAndTo(receiver, sender, sender, receiver);
        List<MessageDTOResponse> messageDTOResponses = new ArrayList<>();
        for (Message message : messageList) {
            messageDTOResponses.add(new MessageDTOResponse(message));
        }

        return messageDTOResponses;
    }

    public List<MessageDTOResponse> getMessageHistory(User receiver, Long sender_id) {
        User sender = userService.getUserById(sender_id);
        List<Message> messageList = messageRepository.findByToAndFrom(receiver, sender);
        List<MessageDTOResponse> messageDTOResponses = new ArrayList<>();
        for (Message message : messageList) {
            messageDTOResponses.add(new MessageDTOResponse(message));
        }

        return messageDTOResponses;
    }

    public List<MessageDTOResponse> getConversations(User receiver) {
        List<Message> messageList = messageRepository.getConversations(receiver.getId());
        List<MessageDTOResponse> messageDTOResponses = new ArrayList<>();
        for (Message message : messageList) {
            messageDTOResponses.add(new MessageDTOResponse(message));
        }
        return messageDTOResponses;
    }

}
