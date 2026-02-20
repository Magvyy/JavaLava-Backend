package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.UnauthenticatedUserException;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public abstract class BaseAuthHController {
    private User getLoggedInUser(CustomUserDetails principal) {
        if (principal == null) return null;
        return principal.getUser();
    }

    protected User throwIfUserNull(CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        if (user == null) {
            throw new UnauthenticatedUserException("User is not authenticated.");
        }
        return user;
    }

    protected Optional<User> getUserIfAuth(CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
