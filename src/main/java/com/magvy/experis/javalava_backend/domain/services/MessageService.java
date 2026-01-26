package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;
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

    private boolean isFriend(User messageSender, User messageReceiver) {
        List<Friend> friendList = friendService.getAllFriendsByUser1(messageSender);

        for (Friend friend : friendList) {
            if (friend.getUser1().equals(messageReceiver) || friend.getUser2().equals(messageReceiver)) {
                return true;
            }
        }
        return false;
    }

    public String sendMessage(String to, String from, String content){
        User userTo = userService.getUserByUsername(to);
        User userFrom = userService.getUserByUsername(from);

        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);

        Message message = new Message(content,  sqlDate,  userFrom, userTo);

//        if (isFriend(message.getFrom(), message.getTo())) {
            messageRepository.save(message);
            return message.getContent();
//        } else {
//            throw new IllegalArgumentException("Can only send messages to friends");
//        }
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




}
