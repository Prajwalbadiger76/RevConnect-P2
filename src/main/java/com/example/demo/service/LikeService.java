package com.example.demo.service;

public interface LikeService {

    void toggleLike(String username, Long postId);

    long getLikeCount(Long postId);

    boolean isLikedByUser(String username, Long postId);
}