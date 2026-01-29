package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.UserAlreadyExistsException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User convertToEntity(AuthDTO authDTO) {
        User user = new User();
        user.setUserName(authDTO.getUserName());
        user.setPassword(passwordEncoder.encode(authDTO.getPassword()));
        return user;
    }

    public ResponseEntity<Map<String, String>> register(AuthDTO authDTO) {
        // Validate input
        if (userRepository.existsByUserName(authDTO.getUserName())) {
            throw new UserAlreadyExistsException("Username is taken");
        }
        User user = convertToEntity(authDTO);
        userRepository.save(user);
        return ResponseEntity.ok(null);
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}
