package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.LoginDTO;
import com.magvy.experis.javalava_backend.application.DTOs.RegisterDTO;
import com.magvy.experis.javalava_backend.application.entitites.User;
import com.magvy.experis.javalava_backend.application.services.UserService;
import com.magvy.experis.javalava_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity <String> LoginPostHandler(@RequestBody LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        return ResponseEntity.ok(jwtUtil.generateToken(loginDTO.getUsername()));
    }

    @PostMapping("/register")
    public ResponseEntity <String> RegisterPostHandler(@RequestBody RegisterDTO registerDTO) {
        ResponseEntity <String> response = userService.register(registerDTO);
        if (response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(registerDTO.getUsername(), registerDTO.getPassword())
            );
            return ResponseEntity.ok(jwtUtil.generateToken(registerDTO.getUsername()));
        }
        return response;
    }
}
