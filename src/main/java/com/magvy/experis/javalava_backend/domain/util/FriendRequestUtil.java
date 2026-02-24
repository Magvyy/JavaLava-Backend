package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.domain.entitites.FriendRequest;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendRequestId;
import com.magvy.experis.javalava_backend.domain.exceptions.FriendRequestException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestUtil {
    private final FriendRequestRepository friendRequestRepository;
    private final SecurityUtil securityUtil;
    private final UserUtil userUtil;

    public FriendRequestUtil(FriendRequestRepository friendRequestRepository, SecurityUtil securityUtil, UserUtil userUtil) {
        this.friendRequestRepository = friendRequestRepository;
        this.securityUtil = securityUtil;
        this.userUtil = userUtil;
    }

    public FriendRequest createFriendRequest(Long userId) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        User friendUser = userUtil.findByIdOrThrow(userId);
        return new FriendRequest(
                authenticatedUser,
                friendUser
        );
    }

    public FriendRequest getFriendRequestFromOrThrow(Long userId) {
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        FriendRequestId id = new FriendRequestId(userId, authenticatedUserId);
        return friendRequestRepository.findById(id).orElseThrow(() -> new FriendRequestException("You don't have a friend request from this user", HttpStatus.NOT_FOUND));
    }

    public boolean friendRequestExists(Long userId) {
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        return friendRequestRepository.existsByFromIdAndToId(authenticatedUserId, userId)
            || friendRequestRepository.existsByFromIdAndToId(userId, authenticatedUserId);
    }

    public boolean hasRequestFrom(Long userId) {
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        return friendRequestRepository.existsByFromIdAndToId(userId, authenticatedUserId);
    }

    public boolean hasRequestTo(Long userId) {
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        return friendRequestRepository.existsByFromIdAndToId(authenticatedUserId, userId);
    }
}
