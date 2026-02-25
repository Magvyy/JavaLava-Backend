package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ConversationDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MessageException;
import com.magvy.experis.javalava_backend.domain.util.MessageUtil;
import com.magvy.experis.javalava_backend.domain.util.SecurityUtil;
import com.magvy.experis.javalava_backend.domain.util.UserUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final SecurityUtil securityUtil;
    private final MessageUtil messageUtil;
    private final UserUtil userUtil;
    private final WebSocketService websocketService;
    private final int pageSize = 20;


    public MessageService(MessageRepository messageRepository, SecurityUtil securityUtil, MessageUtil messageUtil, UserUtil userUtil, WebSocketService websocketService) {
        this.messageRepository = messageRepository;
        this.securityUtil = securityUtil;
        this.messageUtil = messageUtil;
        this.userUtil = userUtil;
        this.websocketService = websocketService;
    }

    public MessageDTOResponse sendMessage(MessageDTORequest messageDTORequest, Long id) {
        messageUtil.validate(messageDTORequest);
        User recipient = userUtil.findByIdOrThrow(id);
        if (messageUtil.isSelf(id)) throw new MessageException("Cannot message self", HttpStatus.BAD_REQUEST);
        Message message = messageUtil.convertToEntity(messageDTORequest, recipient);
        message = messageRepository.save(message);
        websocketService.sendMessage(message.getTo().getUserName(), message);
        return new MessageDTOResponse(message);
    }

    public List<ConversationDTOResponse> getConversations(int offset) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        Sort sort = Sort.by("sent").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);
        Page<Message> messageList = messageRepository.getConversationsOrderBySentDesc(authenticatedUser.getId(), pageable);
        return messageList.stream()
                .map(msg -> new ConversationDTOResponse(msg, authenticatedUser))
                .toList();
    }

    public List<MessageDTOResponse> getConversation(Long id, int offset) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        Sort sort = Sort.by("sent").descending();
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);
        User sender = userUtil.findByIdOrThrow(id);
        Page<Message> messageList = messageRepository.findByFromAndToOrFromAndTo(authenticatedUser, sender, sender, authenticatedUser, pageable);
        return messageUtil.pageToDTOList(messageList).reversed();
    }
}
