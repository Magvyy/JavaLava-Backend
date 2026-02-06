package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.application.security.filter.util.JwtUtil;
import com.magvy.experis.javalava_backend.domain.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity <?> LoginPostHandler(@RequestBody AuthDTO authDTO, HttpServletResponse response) {
        return AuthHandler(authDTO, response);
    }

    @PostMapping("/register")
    public ResponseEntity <?> RegisterPostHandler(@RequestBody AuthDTO authDTO, HttpServletResponse response) {
        userService.register(authDTO);
        return AuthHandler(authDTO, response);
    }

    private ResponseEntity <?> AuthHandler(AuthDTO authDTO, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getUserName(), authDTO.getPassword())
        );
        String jwt = jwtUtil.generateToken(authDTO.getUserName());

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails details) {
            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60);
            response.addCookie(jwtCookie);
        }

        return ResponseEntity.ok("Authenticated");
    }

}