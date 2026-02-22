package com.example.demo.controller;

import com.example.demo.service.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{username}")
    public String follow(@PathVariable String username,
                         Authentication authentication) {

        followService.follow(authentication.getName(), username);
        return "redirect:/profile/" + username;
    }

    @PostMapping("/unfollow/{username}")
    public String unfollow(@PathVariable String username,
                           Authentication authentication) {

        followService.unfollow(authentication.getName(), username);
        return "redirect:/profile/" + username;
    }
}