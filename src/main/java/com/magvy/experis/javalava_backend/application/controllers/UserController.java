package com.magvy.experis.javalava_backend.application.controllers;

import com.magvy.experis.javalava_backend.domain.DTOs.LoginDTO;
import com.magvy.experis.javalava_backend.domain.DTOs.RegisterDTO;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
     @Autowired
     private UserService userService;

    @GetMapping("/login")
    public ResponseEntity <String> LoginGetHandler() {
        return ResponseEntity.ok("Welcome to login page.");
    }

    @PostMapping("/login")
    public ResponseEntity <User> LoginPostHandler(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @GetMapping("/register")
    public ResponseEntity <String> RegisterGetHandler() {
        return ResponseEntity.ok("Welcome to register page.");
    }

    @PostMapping("/register")
    public ResponseEntity <User> RegisterPostHandler(@RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }
}
