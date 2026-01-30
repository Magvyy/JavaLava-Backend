package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserSearchResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.FriendRequest;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendId;
import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendRequestId;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ResponseEntity<Void> sendFriendRequest(User from, Long toId) {
        Long fromId = from.getId();
        if (isFriends(fromId, toId)) {
            return ResponseEntity.badRequest().build();
        }
        if (friendRequestRepository.existsByFromIdAndToId(fromId, toId)
                || friendRequestRepository.existsByFromIdAndToId(toId, fromId)) {
            return ResponseEntity.badRequest().build();
        }
        User to = userService.getUserById(toId);
        FriendRequest friendRequest = new FriendRequest(
                from,
                to
        );
        friendRequestRepository.save(friendRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    public ResponseEntity<Void> acceptFriendRequest(User user, Long fromId) {
        Long userId = user.getId();
        FriendRequest request = getIncomingRequestOrThrow(fromId, userId);
        User friendUser = request.getFrom();
        if (isFriends(user.getId(), friendUser.getId())) {
            friendRequestRepository.delete(request);
            return ResponseEntity.badRequest().build();
        }
        Friend friend = new Friend(user, friendUser);
        friendRepository.save(friend);
        friendRequestRepository.delete(request);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> declineFriendRequest(User user, Long fromId) {
        Long userId = user.getId();
        FriendRequest request = getIncomingRequestOrThrow(fromId, userId);
        friendRequestRepository.delete(request);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> removeFriend(User user, Long friendId) {
        Long userId = user.getId();
        FriendId id = new FriendId(userId, friendId);
        if (!friendRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        friendRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<UserSearchResponse>> getFriendsList(User user) {
        Long id = user.getId();
        List<Friend> friends = friendRepository.findAllByUser(id);
        List<UserSearchResponse> response = friends.stream()
                .map(friend -> {
                    User other =
                            friend.getUser1().getId().equals(id)
                                    ? friend.getUser2()
                                    : friend.getUser1();

                    return new UserSearchResponse(
                            other.getId(),
                            other.getUserName()
                    );
                })
                .toList();
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<UserSearchResponse>> getFriendRequests(User user) {
        Long id = user.getId();
        List<FriendRequest> requests = friendRequestRepository.findAllByToId(id);
        List<UserSearchResponse> response = requests.stream()
                .map(request -> {
                    User from = request.getFrom();
                    return new UserSearchResponse(
                            from.getId(),
                            from.getUserName()
                    );
                })
                .toList();
        return ResponseEntity.ok(response);
    }

    private FriendRequest getIncomingRequestOrThrow(Long fromId, Long toUserId) {

        FriendRequestId id = new FriendRequestId(fromId, toUserId);

        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (!request.getTo().getId().equals(toUserId)) {
            throw new AccessDeniedException("Not authorized to manage this friend request");
        }

        return request;
    }
}
