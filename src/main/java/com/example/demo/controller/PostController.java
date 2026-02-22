package com.example.demo.controller;

import com.example.demo.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/create")
    public String createPage() {
        return "create-post";
    }

    @PostMapping("/create")
    public String createPost(Authentication auth,
                             @RequestParam String content) {

        postService.createPost(auth.getName(), content);

        return "redirect:/feed";
    }
}