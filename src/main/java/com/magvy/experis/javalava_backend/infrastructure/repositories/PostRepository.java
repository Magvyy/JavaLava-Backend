package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUser(User user);
    Page<Post> findByUserId(int userId, Pageable pageable);
    Page<Post> findByVisibleTrue(Pageable pageable);
    Page<Post> findByVisibleTrueAndUserId(int userId, Pageable pageable);


    @Query("""
    SELECT p
    FROM Post p
    WHERE p.visible
       OR p.user.id = :currentUserId
       OR p.user.id IN (
           SELECT CASE
                    WHEN f.user1.id = :currentUserId THEN f.user2.id
                    ELSE f.user1.id
                  END
           FROM Friend f
           WHERE f.user1.id = :currentUserId OR f.user2.id = :currentUserId
       )
    ORDER BY p.published DESC
""")
    Page<Post> findPostsForUser(@Param("currentUserId") int currentUserId, Pageable pageable);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.user.id IN (
        SELECT CASE
                 WHEN f.user1.id = :currentUserId THEN f.user2.id
                 ELSE f.user1.id
               END
        FROM Friend f
        WHERE f.user1.id = :currentUserId
           OR f.user2.id = :currentUserId
    )
    ORDER BY p.published DESC
""")
    Page<Post> findPostsFromFriends(@Param("currentUserId") int currentUserId, Pageable pageable);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.user.id = :targetUserId
      AND (
            p.visible = true
            OR :currentUserId = :targetUserId
            OR EXISTS (
                SELECT 1
                FROM Friend f
                WHERE (f.user1.id = :currentUserId AND f.user2.id = :targetUserId)
                   OR (f.user2.id = :currentUserId AND f.user1.id = :targetUserId)
            )
      )
    ORDER BY p.published DESC
""")
    Page<Post> findPostsFromUser(@Param("currentUserId") int currentUserId,
                                           @Param("targetUserId") int targetUserId, Pageable pageable);
}
