package com.example.demo.service;

public interface CommentService {

    void addComment(String username, Long postId, String content);

    void deleteComment(String username, Long commentId);
}