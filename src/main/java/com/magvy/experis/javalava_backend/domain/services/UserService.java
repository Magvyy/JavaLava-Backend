package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ProfileDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.enums.FriendStatus;
import com.magvy.experis.javalava_backend.domain.exceptions.UserException;
import com.magvy.experis.javalava_backend.domain.util.UserUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserUtil userUtil;
    private final int pageSize = 10;

    public UserService(UserRepository userRepository, UserUtil userUtil) {
        this.userRepository = userRepository;
        this.userUtil = userUtil;
    }

    public void createUser(AuthDTO authDTO) {
        // Validate input
        if (userRepository.existsByUserName(authDTO.getUserName())) {
            throw new UserException("Username is taken", HttpStatus.CONFLICT);
        }
        User user = userUtil.convertToEntity(authDTO);
        userRepository.save(user);
    }

    public UserDTOResponse readUser(Long id) {
        User user = userUtil.getUserById(id);
        return new UserDTOResponse(user);
    }

    public void deleteUser(Long userId, User authUser) {
        User user = userUtil.getUserById(userId);
        if (userUtil.isAdmin(userId)) {
            throw new IllegalArgumentException("Cannot delete admin user");
        }
        if (!authUser.getId().equals(user.getId()) && !userUtil.isAdmin(authUser.getId())) {
            throw new IllegalArgumentException("Cannot delete this user");
        }
        userRepository.delete(user);
    }

    public ProfileDTOResponse getProfile(Long id, FriendStatus friendStatus) {
        User user = userUtil.getUserById(id);
        return new ProfileDTOResponse(user, friendStatus);
    }

    public List<UserDTOResponse> search(String query, int offset) {
        //length longer than 2 to avoid too many results / better performance
        if (query == null) {
            return List.of();
        }

        Pageable limit = PageRequest.of(offset / pageSize, pageSize);
        return userRepository.searchUsers(query.trim(), limit);
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String userName)  {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
        return new CustomUserDetails(user);
    }
    public UserDTOResponse convertToDTO(User user) {
        return new UserDTOResponse(user);
    }
}