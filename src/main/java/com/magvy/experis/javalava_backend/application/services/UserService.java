package com.magvy.experis.javalava_backend.application.services;

import com.magvy.experis.javalava_backend.application.DTOs.LoginDTO;
import com.magvy.experis.javalava_backend.application.DTOs.RegisterDTO;
import com.magvy.experis.javalava_backend.application.entitites.User;
import com.magvy.experis.javalava_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService() {

    }

    public User convertToEntity(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        return user;
    }

    public ResponseEntity <String> register(RegisterDTO registerDTO) {
        // Validate input
        // Check if user exists
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity.status(409).body("Username is taken.");
        }
        // Hash password
        // Create user
        User user = convertToEntity(registerDTO);
        userRepository.save(user);
        return ResponseEntity.ok("");
    }

    public ResponseEntity <User> login(LoginDTO loginDTO) {
        // Validate input
        // Check if user exists
        if (!userRepository.existsByUsername(loginDTO.getUsername())) {
            return ResponseEntity.status(404).body(null);
        }
        User exist = userRepository.findByUsername(loginDTO.getUsername()).get();
        return ResponseEntity.ok(exist);
    }
}
