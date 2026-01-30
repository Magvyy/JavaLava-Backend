package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserSearchResponse;
import com.magvy.experis.javalava_backend.application.security.config.CustomUserDetails;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController extends BaseAuthHController{
    public final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/requests/{userId}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        return friendService.sendFriendRequest(user, userId);
    }
    @GetMapping("/requests")
    public ResponseEntity<List<UserSearchResponse>> getFriendRequests(@AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        return friendService.getFriendRequests(user);
    }
    @PostMapping("/requests/{from_user_id}/accept")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable(name = "from_user_id") Long fromId, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        return friendService.acceptFriendRequest(user, fromId);
    }
    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<Void> declineFriendRequest(@PathVariable Long requestId, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        return friendService.declineFriendRequest(user, requestId);
    }
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long friendId, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        return friendService.removeFriend(user, friendId);
    }
    @GetMapping
    public ResponseEntity<List<UserSearchResponse>> getFriendsList(@AuthenticationPrincipal CustomUserDetails principal) {
        User user = throwIfUserNull(principal);
        return friendService.getFriendsList(user);
    }
}
