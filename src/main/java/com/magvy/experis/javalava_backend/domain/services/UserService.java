package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ProfileDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.enums.FriendStatus;
import com.magvy.experis.javalava_backend.domain.exceptions.UserException;
import com.magvy.experis.javalava_backend.domain.util.SecurityUtil;
import com.magvy.experis.javalava_backend.domain.util.UserUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;
    private final UserUtil userUtil;
    private final FriendService friendService;
    private final int pageSize = 10;

    public UserService(UserRepository userRepository, SecurityUtil securityUtil, UserUtil userUtil, FriendService friendService) {
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
        this.userUtil = userUtil;
        this.friendService = friendService;
    }

    public void createUser(AuthDTO authDTO) {
        userUtil.validate(authDTO);
        if (userUtil.isUserNameTaken(authDTO.getUserName())) throw new UserException("Username is taken", HttpStatus.CONFLICT);
        User user = userUtil.convertToEntity(authDTO);
        userRepository.save(user);
    }

    public UserDTOResponse readUser(Long id) {
        User user = userUtil.findByIdOrThrow(id);
        return new UserDTOResponse(user);
    }

    public void deleteUser(Long userId) throws AccessDeniedException {
        User user = userUtil.findByIdOrThrow(userId);
        if (userUtil.isAdmin(userId)) throw new AccessDeniedException("Cannot delete admin user");
        if (!securityUtil.authenticatedUserHasId(userId) && !securityUtil.authenticatedUserIsAdmin()) throw new AccessDeniedException("Cannot delete this user");
        userRepository.delete(user);
    }

    public ProfileDTOResponse getProfile(Long id) {
        User user = userUtil.findByIdOrThrow(id);
        FriendStatus friendStatus = (securityUtil.isAuthenticated()) ? friendService.getFriendStatus(id) : null;
        return new ProfileDTOResponse(user, friendStatus);
    }

    public List<UserDTOResponse> search(String query, int offset) {
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