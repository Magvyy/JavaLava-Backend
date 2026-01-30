package com.magvy.experis.javalava_backend.infrastructure.readonly;

import com.magvy.experis.javalava_backend.domain.entitites.User;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ReadOnlyUserRepository {
    Optional <User> findById(int userId);
    Optional <User> findByUserName(String userName);
}
