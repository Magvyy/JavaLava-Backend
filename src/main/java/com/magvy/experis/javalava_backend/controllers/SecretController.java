package com.magvy.experis.javalava_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SecretController {

    @GetMapping("/secret")
    public ResponseEntity <String> Secret() {
        return ResponseEntity.ok("Hi, it's me, the secret.");
    }
}
