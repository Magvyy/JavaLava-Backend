package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;


    public MessageService(MessageRepository messageRepository, UserRepository userRepository, FriendRepository friendRepository) {
        this.messageRepository = messageRepository;
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    private boolean isFriend(User messageSender, User messageReceiver) {
        List<Friend> friendList = friendRepository.getAllFriendsByUser1(messageSender);

        for (Friend friend : friendList) {
            if (friend.getUser1().equals(messageReceiver) || friend.getUser2().equals(messageReceiver)) {
                return true;
            }
        }
        return false;
    }

    public String sendMessage(String to, String from, String content){
        User userTo = userRepository.findByUsername(to).orElseThrow();
        User userFrom = userRepository.findByUsername(from).orElseThrow();

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

    public String readNewestMessage(String to, String from){
        User userTo = userRepository.findByUsername(to).orElseThrow();
        User userFrom = userRepository.findByUsername(from).orElseThrow();


        return messageRepository.getMessageByTo(userTo, userFrom).getLast().getContent();
    }

    public List<Message> getAllMessagesToUser(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow();
            return messageRepository.getMessageByTo(user);
    }




}
