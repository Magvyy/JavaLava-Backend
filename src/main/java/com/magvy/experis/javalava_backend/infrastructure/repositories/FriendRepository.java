package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    boolean existsByUser1IdAndUser2Id(int user1Id, int user2Id);
}
