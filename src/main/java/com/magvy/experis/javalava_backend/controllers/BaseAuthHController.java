package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;

public abstract class BaseAuthHController {
    protected User getLoggedInUser(CustomUserDetails principal) {
        if (principal == null) return null;
        return principal.getUser();
    }
}
