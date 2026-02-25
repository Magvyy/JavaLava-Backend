package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.MessageDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MessageException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageUtil {
    private final SecurityUtil securityUtil;

    public MessageUtil(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
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

    public List<MessageDTOResponse> pageToDTOList(Page<Message> messages) {
        return messages.stream()
                .map(MessageDTOResponse::new)
                .toList();
    }
}
