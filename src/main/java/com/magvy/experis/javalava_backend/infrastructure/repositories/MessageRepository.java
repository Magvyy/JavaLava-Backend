package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByToAndFrom(User to, User from, Pageable pageable);
    List<Message> getMessageByFrom(User from, User to);

}

