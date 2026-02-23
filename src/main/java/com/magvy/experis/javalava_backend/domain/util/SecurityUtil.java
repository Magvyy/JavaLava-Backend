package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.UserException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtil {
    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            Optional<User> oUser = userRepository.findByUserName(((UserDetails) authentication.getPrincipal()).getUsername());
            if (oUser.isEmpty()) throw new UserException("User is authenticated, but not in database", HttpStatus.INTERNAL_SERVER_ERROR);
            return oUser.get();
        }
        return null;
    }

    public boolean isAuthenticated() {
        return getAuthenticatedUser() != null;
    }

    public boolean authenticatedUserHasId(Long id) {
        User authenticatedUser = getAuthenticatedUser();
        return authenticatedUser.getId().equals(id);
    }

    public boolean authenticatedUserIsAdmin() {
        Long authenticatedUserId = getAuthenticatedUser().getId();
        return findByIdOrThrow(authenticatedUserId).getRoles().stream().anyMatch(role -> role.getRole() == RoleEnum.ADMIN);
    }

    private User findByIdOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
    }
}
