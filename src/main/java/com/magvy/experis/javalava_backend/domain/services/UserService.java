package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.UserDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ProfileDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.enums.FriendStatus;
import com.magvy.experis.javalava_backend.domain.exceptions.UserException;
import com.magvy.experis.javalava_backend.domain.util.SecurityUtil;
import com.magvy.experis.javalava_backend.domain.util.UserUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserService {
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

    public void createUser(UserDTORequest userDTORequest) {
        userUtil.validateRegister(userDTORequest);
        if (userUtil.isUserNameTaken(userDTORequest.getUserName())) throw new UserException("Username is taken", HttpStatus.CONFLICT);
        User user = userUtil.convertToEntity(userDTORequest);
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

    public UserDTOResponse updateUser(Long id, UserDTORequest userRequestDTO, MultipartFile file) {
        User user = userUtil.findByIdOrThrow(id);
        if (!userUtil.authenticatedUserHasId(id)) throw new UserException("Unauthorized user update", HttpStatus.FORBIDDEN);
        userUtil.validateAndSet(user, userRequestDTO, file);
        user = userRepository.save(user);
        return new UserDTOResponse(user);
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
        return userUtil.pageToDTOList(userRepository.searchUsers(query.trim(), limit));
    }
}