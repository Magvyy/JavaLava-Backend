package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.application.security.filter.util.JwtUtil;
import com.magvy.experis.javalava_backend.domain.services.UserService;
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
    public ResponseEntity <Map<String, String>> LoginPostHandler(@RequestBody AuthDTO authDTO) {
        return AuthHandler(authDTO);
    }

    @PostMapping("/register")
    public ResponseEntity <Map<String, String>> RegisterPostHandler(@RequestBody AuthDTO authDTO) {
        ResponseEntity <Map<String, String>> response = userService.register(authDTO);
        if (response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            return AuthHandler(authDTO);
        }
        return response;
    }

    private ResponseEntity <Map<String, String>> AuthHandler(AuthDTO authDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getUserName(), authDTO.getPassword())
        );
        Map<String, String> response = new HashMap<>();
        response.put("jwt", jwtUtil.generateToken(authDTO.getUserName()));
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails details) {
            response.put("user_id", String.valueOf(details.getUser().getId()));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}