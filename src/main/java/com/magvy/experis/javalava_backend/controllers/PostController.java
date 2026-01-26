package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.outgoingDTO.PostDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.incomingDTO.PostDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.PostService;
import com.magvy.experis.javalava_backend.infrastructure.readonly.ReadOnlyUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final ReadOnlyUserRepository userRepository;

    public PostController(PostService postService, ReadOnlyUserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }


    @PostMapping("/create")
    public HttpStatus LoginPostHandler(@RequestBody PostDTORequest postDTO) {
        if (postService.createPost(postDTO)) return HttpStatus.OK;
        else return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @GetMapping("/user/{id}")
    public List<PostDTOResponse> LoadPostByUserHandler(@PathVariable int id, @RequestParam int page, Authentication auth) {
        User user = getLoggedInUser(auth);
        return postService.loadPostsByUser(page, user, id);
    }

    @GetMapping("/all")
    public List<PostDTOResponse> LoadPostHandler(@RequestParam int page, Authentication auth) {
        User user = getLoggedInUser(auth);
        return postService.loadPosts(page, user);
    }

    @GetMapping("/friends")
    public List<PostDTOResponse> LoadPostByFriendsHandler(@RequestParam int page, Authentication auth) {
        User user = getLoggedInUser(auth);
        return postService.loadPostsByFriends(page, user);
    }

    private User getLoggedInUser(Authentication auth) {
        Object principal = auth.getPrincipal();
        if (principal instanceof String username) {
            Optional<User> oUser = userRepository.findByUsername(username);
            if (oUser.isEmpty()) {
                return null;
            }
            return oUser.get();
        }
        return null;
    }
}