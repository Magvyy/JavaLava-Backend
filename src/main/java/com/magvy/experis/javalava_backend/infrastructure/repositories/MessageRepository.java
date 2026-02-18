package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByToAndFrom(User to, User from, Pageable pageable);
    List<Message> getMessageByFrom(User from, User to);

    Page<Message> findByFromAndToOrFromAndTo(
            User from1, User to1,
            User from2, User to2,
            Pageable pageable
    );

    @Query(value = """
    SELECT distinct on (other_user_id) *
    FROM (
        SELECT *,
            CASE
                WHEN from_user_id = :id THEN to_user_id
                ELSE from_user_id
            END AS other_user_id
        FROM messages
        WHERE from_user_id = :id OR to_user_id = :id
    ) sub
    ORDER BY other_user_id, sent DESC
    LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Message> getConversationsOrderBySentDesc(Long id, int limit, int offset);
}

