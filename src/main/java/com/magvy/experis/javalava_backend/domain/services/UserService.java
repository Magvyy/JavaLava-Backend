package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.domain.DTOs.LoginDTO;
import com.magvy.experis.javalava_backend.domain.DTOs.RegisterDTO;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService() {

    }

    public User convertToEntity(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setHashedPassword(registerDTO.getPassword());
        return user;
    }

    public User convertToEntity(LoginDTO loginDTO) {
        User user = new User();
        user.setUsername(loginDTO.getUsername());
        user.setHashedPassword(loginDTO.getPassword());
        return user;
    }

    public ResponseEntity<User> register(RegisterDTO registerDTO) {
        // Validate input
        // Check if user exists
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity.status(409).body(null);
        }
        // Hash password
        // Create user
        User user = convertToEntity(registerDTO);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<User> login(LoginDTO loginDTO) {
        // Validate input
        // Check if user exists
        if (!userRepository.existsByUsername(loginDTO.getUsername())) {
            return ResponseEntity.status(404).body(null);
        }
        User login = convertToEntity(loginDTO);
        User exist = userRepository.findByUsername(login.getUsername()).get();
        // Hash password
        if (!login.getHashedPassword().equals(exist.getHashedPassword())) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(exist);
    }
}
