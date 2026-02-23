package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.custom.CustomUserDetails;
import com.magvy.experis.javalava_backend.controllers.util.ResponseUtil;
import com.magvy.experis.javalava_backend.domain.services.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController extends BaseAuthController {
    public final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/requests/{userId}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        friendService.sendFriendRequest(userId);
        return ResponseUtil.wrapEntity(null);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<UserDTOResponse>> getFriendRequests(@AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        List<UserDTOResponse> friendRequests = friendService.getFriendRequests();
        return ResponseUtil.wrapEntity(friendRequests);
    }

    @PostMapping("/requests/{from_user_id}/accept")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable(name = "from_user_id") Long fromId, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        friendService.acceptFriendRequest(fromId);
        return ResponseUtil.wrapEntity(null);
    }

    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<Void> declineFriendRequest(@PathVariable Long requestId, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        friendService.declineFriendRequest(requestId);
        return ResponseUtil.wrapEntity(null);
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long friendId, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        friendService.removeFriend(friendId);
        return ResponseUtil.wrapEntity(null);
    }

    @GetMapping
    public ResponseEntity<List<UserDTOResponse>> getFriendsList(@AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        List<UserDTOResponse> friends = friendService.getFriendsList();
        return ResponseUtil.wrapEntity(friends);
    }
}
