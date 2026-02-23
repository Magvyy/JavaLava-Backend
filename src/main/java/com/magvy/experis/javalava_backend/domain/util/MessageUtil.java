package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MessageException;
import com.magvy.experis.javalava_backend.domain.exceptions.PostException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.MessageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
    private final MessageRepository messageRepository;
    private final SecurityUtil securityUtil;
    private final FriendUtil friendUtil;

    public MessageUtil(MessageRepository messageRepository, SecurityUtil securityUtil, FriendUtil friendUtil) {
        this.messageRepository = messageRepository;
        this.securityUtil = securityUtil;
        this.friendUtil = friendUtil;
    }

    public Message convertToEntity(MessageDTORequest messageDTORequest, User recipient) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        return new Message(
                messageDTORequest.getContent(),
                authenticatedUser,
                recipient
        );
    }

    public void validate(MessageDTORequest messageDTORequest) {
        if (!isValidContent(messageDTORequest.getContent())) throw new MessageException("Invalid content", HttpStatus.BAD_REQUEST);
    }

    private boolean isValidContent(String content) {
        return !content.trim().isEmpty();
    }

    public boolean isSelf(Long userId) {
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        return authenticatedUserId.equals(userId);
    }
}
