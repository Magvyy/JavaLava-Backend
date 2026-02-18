package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ProfileDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.enums.FriendStatus;
import com.magvy.experis.javalava_backend.domain.exceptions.UserAlreadyExistsException;
import com.magvy.experis.javalava_backend.domain.exceptions.UserNotFoundException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean isAdmin(Long id) {
        return getUserById(id).getRoles().stream().anyMatch(role -> role.getRole() == RoleEnum.ADMIN);
    }

    public User convertToEntity(AuthDTO authDTO) {
        return new User(
                authDTO.getUserName(),
                passwordEncoder.encode(authDTO.getPassword())
        );
    }

    public void register(AuthDTO authDTO) {
        // Validate input
        if (userRepository.existsByUserName(authDTO.getUserName())) {
            throw new UserAlreadyExistsException("Username is taken");
        }
        User user = convertToEntity(authDTO);
        userRepository.save(user);
    }

    public List<UserDTOResponse> search(String query, int page) {
        //length longer than 2 to avoid too many results / better performance
        if (query == null) {
            return List.of();
        }

        Pageable limit = PageRequest.of(page, 10);
        return userRepository.searchUsers(query.trim(), limit);
    }
    public ProfileDTOResponse getProfile(Long id, FriendStatus friendStatus) {
        User user = getUserById(id);
        return new ProfileDTOResponse(user, friendStatus);
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

    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        if (isAdmin(userId)) {
            throw new IllegalArgumentException("Cannot delete admin user");
        }
        userRepository.delete(user);
    }
}