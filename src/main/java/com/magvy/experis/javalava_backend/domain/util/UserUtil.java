package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.PostException;
import com.magvy.experis.javalava_backend.domain.exceptions.UserException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUtil {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserUtil(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User convertToEntity(AuthDTO authDTO) {
        return new User(
                authDTO.getUserName(),
                passwordEncoder.encode(authDTO.getPassword())
        );
    }

    public boolean isUserNameTaken(String userName) {
        Optional<User> oUser = userRepository.findByUserName(userName);
        return oUser.isPresent();
    }

    public User findByIdOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
    }

    public boolean isAdmin(Long id) {
        return findByIdOrThrow(id).getRoles().stream().anyMatch(role -> role.getRole() == RoleEnum.ADMIN);
    }

    public boolean isValidUserName(String userName) {
        return !userName.trim().isEmpty();
    }

    public boolean isValidPassword(String userName) {
        return !userName.trim().isEmpty();
    }
}
