package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByToAndFrom(User to, User from);
    List<Message> getMessageByFrom(User from, User to);

    @Query(value = """
    SELECT DISTINCT ON (other_user_id) *
    FROM (
        SELECT *,
            CASE
                WHEN from_user_id = :id THEN to_user_id
                ELSE from_user_id
            END AS other_user_id
        FROM messages
        WHERE from_user_id = :id OR to_user_id = :id
    ) sub
    ORDER BY other_user_id, sent ASC;
    """, nativeQuery = true)
    List<Message> getConversations(Long id);
}

