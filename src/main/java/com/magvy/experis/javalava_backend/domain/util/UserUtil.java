package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.UserNotFoundException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserUtil(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public boolean isAdmin(Long id) {
        return getUserById(id).getRoles().stream().anyMatch(role -> role.getRole() == RoleEnum.ADMIN);
    }

    public User convertToEntity(AuthDTO authDTO) {
        return new User(
                authDTO.getUserName(),
                passwordEncoder.encode(authDTO.getPassword())
        );
    }
}
