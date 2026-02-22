package com.example.demo.controller;

import com.example.demo.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // ================= ADD COMMENT =================

    @PostMapping("/{postId}")
    public String addComment(@PathVariable Long postId,
                             @RequestParam String content,
                             Authentication authentication,
                             @RequestHeader(value = "Referer", required = false) String referer) {

        commentService.addComment(
                authentication.getName(),
                postId,
                content
        );

        return "redirect:" + (referer != null ? referer : "/feed");
    }

    // ================= DELETE COMMENT =================

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId,
                                Authentication authentication,
                                @RequestHeader(value = "Referer", required = false) String referer) {

        commentService.deleteComment(
                authentication.getName(),
                commentId
        );

        return "redirect:" + (referer != null ? referer : "/feed");
    }
}