package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingUserException;

import java.util.Optional;

public abstract class BaseAuthHController {
    private User getLoggedInUser(CustomUserDetails principal) {
        if (principal == null) return null;
        return principal.getUser();
    }

    protected User throwIfUserNull(CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        if (user == null) {
            throw new MissingUserException("User not found even though authenticated");
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
