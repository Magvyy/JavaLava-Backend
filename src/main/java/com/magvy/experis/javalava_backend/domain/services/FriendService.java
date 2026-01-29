package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserSearchResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.FriendRequest;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRequestRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    public FriendService(FriendRepository friendRepository, FriendRequestRepository friendRequestRepository, UserService userService) {
        this.userService = userService;
        this.friendRepository = friendRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    public boolean isFriends(Long userId1, Long userId2) {
        return friendRepository.existsByUser1IdAndUser2Id(userId1, userId2)
                || friendRepository.existsByUser1IdAndUser2Id(userId2, userId1);
    }

    public ResponseEntity<Void> sendFriendRequest(User user, Long userId) {
        Long id = user.getId();
        if(isFriends(id, userId)) {
            return ResponseEntity.badRequest().build();
        }
        if(friendRequestRepository.existsByFromIdAndToId(id, userId)
                || friendRequestRepository.existsByFromIdAndToId(userId, id)) {
            return ResponseEntity.badRequest().build();
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setFrom(user);
        friendRequest.setTo(userService.getUserById(userId));
        friendRequestRepository.save(friendRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    public ResponseEntity<Void> acceptFriendRequest(Long id, Long requestId) {
    }

    public ResponseEntity<Void> declineFriendRequest(Long id, Long requestId) {
    }

    public ResponseEntity<Void> removeFriend(Long id, Long friendId) {
    }

    public ResponseEntity<List<UserSearchResponse>> getFriendsList(Long id) {
    }
}
