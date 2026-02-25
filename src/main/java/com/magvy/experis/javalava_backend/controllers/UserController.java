package com.magvy.experis.javalava_backend.controllers;


import com.magvy.experis.javalava_backend.application.DTOs.incoming.UserDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.ProfileDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.application.security.config.custom.CustomUserDetails;
import com.magvy.experis.javalava_backend.controllers.util.ResponseUtil;
import com.magvy.experis.javalava_backend.domain.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends BaseAuthController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTOResponse>> searchUsers(@RequestParam String q, @RequestParam int offset) {
        List<UserDTOResponse> userDTOResponses = userService.search(q, offset);
        return ResponseUtil.wrapEntity(userDTOResponses);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTOResponse> getUser(@PathVariable Long userId) {
        UserDTOResponse userDTOResponse = userService.readUser(userId);
        return ResponseUtil.wrapEntity(userDTOResponse);
    }

    @PutMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDTOResponse> updateUser(@PathVariable Long userId, @RequestPart(value = "attachment", required = false) MultipartFile file, @RequestPart (value = "user") UserDTORequest userDTORequest, @AuthenticationPrincipal CustomUserDetails principal) {
        throwIfUserNull(principal);
        UserDTOResponse userDTOResponse = userService.updateUser(userId, userDTORequest, file);
        return ResponseUtil.wrapEntity(userDTOResponse);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ProfileDTOResponse> getProfile(@PathVariable Long userId) {
        ProfileDTOResponse profileDTOResponse = userService.getProfile(userId);
        return ResponseUtil.wrapEntity(profileDTOResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails principal) throws AccessDeniedException {
        throwIfUserNull(principal);
        userService.deleteUser(userId);
        return ResponseUtil.wrapEntity(null);
    }
}
