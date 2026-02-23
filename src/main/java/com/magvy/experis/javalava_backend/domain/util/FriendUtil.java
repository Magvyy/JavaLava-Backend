package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendId;
import com.magvy.experis.javalava_backend.domain.exceptions.FriendException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FriendUtil {
    private final FriendRepository friendRepository;
    private final SecurityUtil securityUtil;
    private final UserUtil userUtil;

    public FriendUtil(FriendRepository friendRepository, SecurityUtil securityUtil, UserUtil userUtil) {
        this.friendRepository = friendRepository;
        this.securityUtil = securityUtil;
        this.userUtil = userUtil;
    }

    public Friend createFriend(Long friendId) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        User friendUser = userUtil.findByIdOrThrow(friendId);
        return new Friend(
                authenticatedUser,
                friendUser
        );
    }

    public FriendId createFriendId(Long friendId) {
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        return new FriendId(
                authenticatedUserId,
                friendId
        );
    }

    public boolean isFriends(Long userId) {
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        return friendRepository.existsByUser1IdAndUser2Id(authenticatedUserId, userId)
            || friendRepository.existsByUser1IdAndUser2Id(userId, authenticatedUserId);
    }

    public Friend findByIdOrThrow(FriendId id) {
        return friendRepository.findById(id).orElseThrow(() -> new FriendException("Friend not found", HttpStatus.NOT_FOUND));
    }
}
