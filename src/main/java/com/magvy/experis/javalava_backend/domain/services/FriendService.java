package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.FriendRequest;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendId;
import com.magvy.experis.javalava_backend.domain.enums.FriendStatus;
import com.magvy.experis.javalava_backend.domain.exceptions.FriendException;
import com.magvy.experis.javalava_backend.domain.util.FriendRequestUtil;
import com.magvy.experis.javalava_backend.domain.util.FriendUtil;
import com.magvy.experis.javalava_backend.domain.util.SecurityUtil;
import com.magvy.experis.javalava_backend.domain.util.UserUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final SecurityUtil securityUtil;
    private final FriendUtil friendUtil;
    private final FriendRequestUtil friendRequestUtil;
    private final UserUtil userUtil;
    private final WebSocketService webSocketService;

    public FriendService(FriendRepository friendRepository, FriendRequestRepository friendRequestRepository, SecurityUtil securityUtil, FriendUtil friendUtil, FriendRequestUtil friendRequestUtil, UserUtil userUtil, WebSocketService webSocketService) {
        this.friendRepository = friendRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.securityUtil = securityUtil;
        this.friendUtil = friendUtil;
        this.friendRequestUtil = friendRequestUtil;
        this.userUtil = userUtil;
        this.webSocketService = webSocketService;
    }

    public void sendFriendRequest(Long recipientId) {
        User recipient = userUtil.findByIdOrThrow(recipientId);
        if (friendUtil.isFriends(recipientId)) throw new FriendException("These users are already friends", HttpStatus.CONFLICT);
        if (friendRequestUtil.friendRequestExists(recipientId)) throw new FriendException("There is already a friend request between these users", HttpStatus.CONFLICT);
        FriendRequest friendRequest = friendRequestUtil.createFriendRequest(recipientId);
        friendRequestRepository.save(friendRequest);
        webSocketService.sendNotification(
                recipient.getUserName(),
                "New friend request from " + friendRequest.getFrom().getUserName()
        );
    }

    @Transactional
    public void acceptFriendRequest(Long senderId) {
        FriendRequest request = friendRequestUtil.getFriendRequestFromOrThrow(senderId);
        User friendUser = request.getFrom();
        if (friendUtil.isFriends(friendUser.getId())) {
            friendRequestRepository.delete(request);
            return;
        }
        Friend friend = friendUtil.createFriend(senderId);
        friendRepository.save(friend);
        friendRequestRepository.delete(request);
        webSocketService.sendNotification(
                friendUser.getUserName(),
                "Your friend request to " + request.getTo().getUserName() + " has been accepted"
        );
    }

    public void declineFriendRequest(Long senderId) {
        FriendRequest request = friendRequestUtil.getFriendRequestFromOrThrow(senderId);
        friendRequestRepository.delete(request);
    }

    public void removeFriend(Long friendId) {
        FriendId id = friendUtil.createFriendId(friendId);
        friendUtil.findByIdOrThrow(id);
        friendRepository.deleteById(id);
    }

    public List<UserDTOResponse> getFriendsList() {
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        List<Friend> friends = friendRepository.findAllByUser(authenticatedUserId);
        return friends.stream()
                .map(friend -> {
                    User other =
                            friend.getUser1().getId().equals(authenticatedUserId)
                                    ? friend.getUser2()
                                    : friend.getUser1();

                    return new UserDTOResponse(other);
                })
                .toList();
    }

    public List<UserDTOResponse> getFriendRequests() {
        Long authenticatedUserId = securityUtil.getAuthenticatedUser().getId();
        List<FriendRequest> requests = friendRequestRepository.findAllByToId(authenticatedUserId);
        return requests.stream()
                .map(request -> {
                    User from = request.getFrom();
                    return new UserDTOResponse(from);
                })
                .toList();
    }

    public FriendStatus getFriendStatus(Long userId) {
        if (friendUtil.isFriends(userId)) {
            return FriendStatus.FRIENDS;
        }
        if (friendRequestUtil.hasRequestTo(userId)) {
            return FriendStatus.PENDING;
        }
        if (friendRequestUtil.hasRequestFrom(userId)) {
            return FriendStatus.REQUESTED;
        }
        return FriendStatus.NOT_FRIENDS;
    }
}
