package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.LoginDTO;
import com.magvy.experis.javalava_backend.application.DTOs.RegisterDTO;
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
@CrossOrigin(origins = "http://localhost:5173/secret")
public class SecretController {

    @GetMapping("/secret")
    public ResponseEntity <String> Secret() {
        return  ResponseEntity.ok("Hi, it's me, the secret.");
    }

}
