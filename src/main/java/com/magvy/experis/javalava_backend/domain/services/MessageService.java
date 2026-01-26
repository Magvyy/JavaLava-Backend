package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.MessageDTO;
import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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

    private Message ConvertToEntity(MessageDTO messageDTO, User sender, User receiver) {
        return new Message(messageDTO.getContent(), (Date) messageDTO.getDate(), sender, receiver);
    }


    public Message sendMessage(MessageDTO messageDTO, User sender, User receiver) {
        Message message = ConvertToEntity(messageDTO, sender,receiver);
        return messageRepository.save(message);
    }

    public List<Message> getMessageHistory(String to, String from){
        User userTo = userService.getUserByUsername(to);
        User userFrom = userService.getUserByUsername(from);

        return messageRepository.getMessageByTo(userTo, userFrom);
    }


    public List<Message> getAllMessagesToUser(String user) {
        User recipient = userService.getUserByUsername(user);
            return messageRepository.getMessageByTo(recipient);
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
