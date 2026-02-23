package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ConversationDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.util.UserUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserUtil userUtil;
    private final int pageSize = 20;


    public MessageService(MessageRepository messageRepository, UserUtil userUtil) {
        this.messageRepository = messageRepository;
        this.userUtil = userUtil;
    }

    private Message ConvertToEntity(MessageDTORequest messageDTORequest, User sender, User recipient) {
        return new Message(
                messageDTORequest.getContent(),
                sender,
                recipient
        );
    }

    public Message sendMessage(MessageDTORequest messageDTORequest, User sender) {
        User recipient = userUtil.findByIdOrThrow(messageDTORequest.getToUserId());

        if (messageDTORequest.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content must be something.");
        }

        if (recipient == null) {
            throw new IllegalArgumentException("Recipient cannot be null");
        }

        if (sender.equals(recipient)) {
            throw new IllegalArgumentException("Sender and recipient are the same");
        }
        Message message = ConvertToEntity(messageDTORequest, sender, recipient);
        return messageRepository.save(message);
    }

    public List<ConversationDTOResponse> getConversations(User authUser, int offset) {
        Sort sort = Sort.by("sent").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);
        Page<Message> messageList = messageRepository.getConversationsOrderBySentDesc(authUser.getId(), pageable);
        return messageList.stream()
                .map(msg -> new ConversationDTOResponse(msg, authUser))
                .toList();
    }

    public List<MessageDTOResponse> getConversation(User recipient, Long sender_id, int offset) {
        Sort sort = Sort.by("sent").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);
        User sender = userUtil.findByIdOrThrow(sender_id);
        Page<Message> messageList = messageRepository.findByFromAndToOrFromAndTo(recipient, sender, sender, recipient, pageable);
        return pageToDTOList(messageList).reversed();
    }

    private List<MessageDTOResponse> pageToDTOList(Page<Message> messages) {
        return messages.stream()
                .map(MessageDTOResponse::new)
                .toList();
    }


}
