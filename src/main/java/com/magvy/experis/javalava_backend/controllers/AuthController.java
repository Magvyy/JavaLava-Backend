package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.AuthDTO;
import com.magvy.experis.javalava_backend.domain.services.UserService;
import com.magvy.experis.javalava_backend.application.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword())
        );
        Map<String, String> response = new HashMap<>();
        response.put("jwt", jwtUtil.generateToken(authDTO.getUsername()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}