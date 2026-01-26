package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.FriendRequestRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;

    public FriendService(FriendRepository friendRepository, FriendRequestRepository friendRequestRepository) {
        this.friendRepository = friendRepository;
        this.friendRequestRepository = friendRequestRepository;
    }


    public List<Friend> getAllFriendsByUser1(User user) {
        return friendRepository.getAllFriendsByUser1(user);
    }
}
