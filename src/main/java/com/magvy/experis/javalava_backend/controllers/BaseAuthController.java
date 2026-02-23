package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class BaseAuthController {
    private User getLoggedInUser(CustomUserDetails principal) {
        if (principal == null) return null;
        return principal.getUser();
    }

    protected User throwIfUserNull(CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        if (user == null) {
            throw new UserException("User is not authenticated.", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
