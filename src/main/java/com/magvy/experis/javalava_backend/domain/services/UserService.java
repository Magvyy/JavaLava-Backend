package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.UserAlreadyExistsException;
import com.magvy.experis.javalava_backend.domain.exceptions.UserNotFoundException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
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

    public List<UserDTOResponse> search(String query) {
        //length longer than 2 to avoid too many results / better performance
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }

        Pageable limit = PageRequest.of(0, 10);
        return userRepository.searchUsers(query.trim(), limit);
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
    public UserDTOResponse convertToDTO(User user) {
        return new UserDTOResponse(user);
    }
}
