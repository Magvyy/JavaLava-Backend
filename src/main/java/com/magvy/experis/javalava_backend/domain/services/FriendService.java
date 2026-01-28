package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRequestRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;

    public FriendService(FriendRepository friendRepository, FriendRequestRepository friendRequestRepository) {
        this.friendRepository = friendRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    public boolean isFriends(int userId1, int userId2) {
        return friendRepository.existsByUser1IdAndUser2Id(userId1, userId2)
                || friendRepository.existsByUser1IdAndUser2Id(userId2, userId1);
    }
}
