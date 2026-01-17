package com.magvy.experis.javalava_backend.config;

import com.magvy.experis.javalava_backend.application.services.CustomUserDetailsService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

@Component
public class Authenticator implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Your custom authentication logic goes here
        // e.g., call a third-party API, query a database
        String username = authentication.getName();
        String password = Objects.requireNonNull(authentication.getCredentials()).toString();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        // Specify the type of Authentication object this provider supports
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
