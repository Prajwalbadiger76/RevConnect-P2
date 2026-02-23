package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.repo.PostRepository;
import com.example.demo.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    public PostController(PostService postService,
                          PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    // ================= CREATE =================

    @GetMapping("/new")
    public String createPostPage() {
        return "create-post";
    }

    @PostMapping("/create")
    public String createPost(@RequestParam String content,
                             Authentication authentication) {

        postService.createPost(authentication.getName(), content);
        return "redirect:/feed";
    }

    // ================= EDIT =================

    @GetMapping("/edit/{id}")
    public String editPostPage(@PathVariable Long id,
                               Authentication authentication,
                               Model model) {

        Post post = postRepository.findById(id)
                .orElseThrow();

        // 🔒 Security check
        if (!post.getUser().getUsername()
                .equals(authentication.getName())) {
            return "redirect:/feed";
        }

        model.addAttribute("post", post);
        return "edit-post";
    }

    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id,
                             @RequestParam String content,
                             Authentication authentication) {

        postService.updatePost(id, content, authentication.getName());
        return "redirect:/feed";
    }

    // ================= DELETE =================

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id,
                             Authentication authentication) {

        postService.deletePost(id, authentication.getName());
        return "redirect:/feed";
    }

    // ================= SHARE =================

    @PostMapping("/share/{id}")
    public String sharePost(@PathVariable Long id,
                            Authentication authentication) {

        postService.sharePost(id, authentication.getName());
        return "redirect:/feed";
    }
}