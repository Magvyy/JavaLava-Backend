package com.magvy.experis.javalava_backend.controllers;


import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.domain.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public List<UserDTOResponse> searchUsers(@RequestParam String q){
        return userService.search(q);
    }

    @GetMapping("/{userId}")
    public UserDTOResponse getUserById(@PathVariable Long userId){
        return userService.convertToDTO(userService.getUserById(userId));
    }
}
