package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repo.CommentRepository;
import com.example.demo.repo.PostRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    public CommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              PostRepository postRepository,
                              NotificationService notificationService) {

        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.notificationService = notificationService;
    }

    // ================= ADD COMMENT =================

    @Override
    public void addComment(String username, Long postId, String content) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        Post post = postRepository.findById(postId)
                .orElseThrow();

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setPost(post);

        commentRepository.save(comment);

        // 🔔 CREATE COMMENT NOTIFICATION
        notificationService.createNotification(
                post.getUser().getUsername(),  // recipient (post owner)
                username,                      // sender
                "COMMENT",
                post.getId()
        );
    }

    // ================= DELETE COMMENT =================

    @Override
    public void deleteComment(String username, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();

        // 🔒 Security check
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You cannot delete this comment");
        }

        commentRepository.delete(comment);
    }
}