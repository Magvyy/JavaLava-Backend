package com.magvy.experis.javalava_backend.infrastructure.repositories;

import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserSearchResponse;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.readonly.ReadOnlyUserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, ReadOnlyUserRepository {

    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    @Query("""
        SELECT new com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserSearchResponse(
            u.id,
            u.username
        )
        FROM User u
        WHERE
            LOWER(u.username) LIKE LOWER(CONCAT(:query, '%'))
    """)
    List<UserSearchResponse> searchUsers(@Param("query") String query, Pageable pageable);
}
