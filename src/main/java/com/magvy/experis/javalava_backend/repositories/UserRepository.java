package com.magvy.experis.javalava_backend.repositories;

import com.magvy.experis.javalava_backend.application.entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Use Spring Data's 'existsBy{Property}' convention so the framework parses this correctly
    boolean existsByUsername(String username);

    // Return Optional to express possible absence
    Optional<User> findByUsername(String username);
}
