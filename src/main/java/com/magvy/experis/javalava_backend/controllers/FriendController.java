package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserSearchResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/friends")
public class FriendController extends BaseAuthHController{
    public final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/requests/{userId}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        return friendService.sendFriendRequest(user.getId(), userId);
    }
    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        return friendService.acceptFriendRequest(user.getId(), requestId);
    }
    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<Void> declineFriendRequest(@PathVariable Long requestId, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        return friendService.declineFriendRequest(user.getId(), requestId);
    }
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long friendId, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        return friendService.removeFriend(user.getId(), friendId);
    }
    @GetMapping
    public ResponseEntity<List<UserSearchResponse>> getFriendsList(@AuthenticationPrincipal CustomUserDetails principal) {
        User user = getLoggedInUser(principal);
        return friendService.getFriendsList(user.getId());
    }
}
