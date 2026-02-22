package com.example.demo.controller;

import com.example.demo.service.LikeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}")
    public String toggleLike(@PathVariable Long postId,
                             Authentication authentication,
                             @RequestHeader(value = "Referer", required = false) String referer) {

        likeService.toggleLike(authentication.getName(), postId);

        return "redirect:" + (referer != null ? referer : "/feed");
    }
}