package com.example.demo.controller;

import com.example.demo.service.PostService;
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
    public String feed(Model model) {

        model.addAttribute("posts",
                postService.getAllPosts());

        return "feed";
    }
}