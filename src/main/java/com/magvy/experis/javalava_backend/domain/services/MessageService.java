package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.MessageDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final FriendService friendService;
    private final UserService userService;


    public MessageService(MessageRepository messageRepository, FriendService friendService, UserService userService) {
        this.messageRepository = messageRepository;
        this.friendService = friendService;
        this.userService = userService;
    }

    private Message ConvertToEntity(MessageDTORequest messageDTORequest, User sender) {
        User receiver = userService.getUserById(messageDTORequest.getId());
        return new Message(messageDTORequest.getContent(), messageDTORequest.getDate(), sender, receiver);
    }

    public Message sendMessage(MessageDTORequest messageDTORequest, User sender) {
        User recipient = userService.getUserById(messageDTORequest.getId());

        if (recipient== null) {
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


    public List<MessageDTOResponse> getMessageHistory(User receiver, int sender_id) {
        User sender = userService.getUserById(sender_id);
        List<Message> messageList = messageRepository.getMessageByTo(receiver, sender);
        List<MessageDTOResponse> messageDTOResponses = new ArrayList<>();
        for (Message message : messageList) {
            messageDTOResponses.add(new MessageDTOResponse(message));
        }

        return messageDTOResponses;
    }


    private boolean isFriend(User messageSender, User messageReceiver) {
        List<Friend> friendList = friendService.getAllFriendsByUser1(messageSender);

        for (Friend friend : friendList) {
            if (friend.getUser1().equals(messageReceiver) || friend.getUser2().equals(messageReceiver)) {
                return true;
            }
        }
        return false;
    }




}
