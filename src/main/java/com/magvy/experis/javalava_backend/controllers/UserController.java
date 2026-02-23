package com.magvy.experis.javalava_backend.controllers;


import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ProfileDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.controllers.util.ResponseUtil;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.enums.FriendStatus;
import com.magvy.experis.javalava_backend.domain.services.FriendService;
import com.magvy.experis.javalava_backend.domain.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController extends BaseAuthController {

    private final UserService userService;
    private final FriendService friendService;

    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTOResponse>> searchUsers(@RequestParam String q, @RequestParam int offset) {
        List<UserDTOResponse> userDTOResponses = userService.search(q, offset);
        return ResponseUtil.wrapEntityList(userDTOResponses);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTOResponse> getUserById(@PathVariable Long userId){
        UserDTOResponse userDTOResponse = userService.readUser(userId);
        return ResponseUtil.wrapEntity(userDTOResponse);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ProfileDTOResponse> getUserById(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails principal) {
        Optional<User> user = getUserIfAuth(principal);
        if (user.isEmpty()) {
            return ResponseUtil.wrapEntity(userService.getProfile(userId, null));
        }
        FriendStatus friendStatus = friendService.getFriendStatus(userId, user.get());
        ProfileDTOResponse profileDTOResponse = userService.getProfile(userId, friendStatus);
        return ResponseUtil.wrapEntity(profileDTOResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{userId}")
    public HttpStatus deleteUser(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails principal) throws AccessDeniedException {
        throwIfUserNull(principal);
        userService.deleteUser(userId);
        return HttpStatus.OK;
    }
}
