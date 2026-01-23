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


    @Query("""
    SELECT p
    FROM Post p
    WHERE p.visible = true
       OR (p.visible = false AND p.user.id IN (
           SELECT CASE
                    WHEN f.user_id_1 = :currentUserId THEN f.user_id_2
                    ELSE f.user_id_1
                  END
           FROM Friend f
           WHERE f.user_id_1 = :currentUserId OR f.user_id_2 = :currentUserId
       ))
    ORDER BY p.published DESC
""")
    Page<Post> findPostsForUser(@Param("currentUserId") int currentUserId, Pageable pageable);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.user.id IN (
        SELECT CASE
                 WHEN f.user_id_1 = :currentUserId THEN f.user_id_2
                 ELSE f.user_id_1
               END
        FROM Friend f
        WHERE f.user_id_1 = :currentUserId
           OR f.user_id_2 = :currentUserId
    )
    ORDER BY p.published DESC
""")
    Page<Post> findPostsFromFriends(@Param("currentUserId") int currentUserId, Pageable pageable);
}
