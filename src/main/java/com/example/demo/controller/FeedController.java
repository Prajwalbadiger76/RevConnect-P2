package com.example.demo.controller;

import com.example.demo.service.PostService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeedController {

    private final PostService postService;

    public FeedController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/feed")
    public String feed(Authentication authentication, Model model) {

        String username = authentication.getName();

        model.addAttribute("posts",
                postService.getFeedPosts(username));

        return "feed";
    }
}