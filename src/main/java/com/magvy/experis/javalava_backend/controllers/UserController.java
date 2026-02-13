package com.magvy.experis.javalava_backend.controllers;


import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ProfileDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.enums.FriendStatus;
import com.magvy.experis.javalava_backend.domain.services.FriendService;
import com.magvy.experis.javalava_backend.domain.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController extends BaseAuthHController {

    private final UserService userService;
    private final FriendService friendService;

    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping("/search")
    public List<UserDTOResponse> searchUsers(@RequestParam String q){
        return userService.search(q);
    }

    @GetMapping("/{userId}")
    public UserDTOResponse getUserById(@PathVariable Long userId){
        return userService.convertToDTO(userService.getUserById(userId));
    }
    @GetMapping("/profile/{userId}")
    public ProfileDTOResponse getUserById(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails principal){
        Optional<User> user = getUserIfAuth(principal);
        if (user.isEmpty()) {
            return userService.getProfile(userId, null);
        }
        FriendStatus friendStatus = friendService.getFriendStatus(userId, user.get());
        return userService.getProfile(userId, friendStatus);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }
}