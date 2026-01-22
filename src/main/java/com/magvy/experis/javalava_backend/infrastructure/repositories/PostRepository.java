package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUser(User user);
}
