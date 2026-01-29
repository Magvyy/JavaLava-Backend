package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.domain.services.FriendService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/comment")
public class FriendController extends BaseAuthHController{
    public final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }
}
