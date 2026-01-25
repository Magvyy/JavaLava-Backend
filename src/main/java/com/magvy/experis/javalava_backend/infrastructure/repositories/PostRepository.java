package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUser(User user);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.id = :id
        AND (
                p.visible OR
                p.user.id = :#{#user.id}
                OR EXISTS (
                    SELECT 1
                    FROM Friend f
                    WHERE (f.user1.id = :#{#user.id} AND f.user2.id = p.user.id)
                       OR (f.user2.id = :#{#user.id} AND f.user1.id = p.user.id)
                )
        )
    """)
    Optional<Post> findById(@Param("user") User user, @Param("id") int id);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.id = :id
    AND p.visible
    """)
    Optional<Post> findById(@Param("id") int id);
}
