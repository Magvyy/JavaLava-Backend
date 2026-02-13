package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.domain.entitites.Friend;
import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {
    boolean existsByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    @Query("""
    SELECT f
    FROM Friend f
    WHERE f.user1.id = :userId OR f.user2.id = :userId
""")
    List<Friend> findAllByUser(@Param("userId") Long userId);
}
