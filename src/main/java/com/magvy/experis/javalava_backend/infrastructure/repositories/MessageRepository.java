package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> getMessageByTo(User to);
    List<Message> getMessageByFrom(User from);
    List<Message> getMessageByTo(User to, User from);
    List<Message> getMessageByFrom(User from, User to);


}
