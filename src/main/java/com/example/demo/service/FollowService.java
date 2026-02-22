package com.example.demo.service;

public interface FollowService {

    void follow(String currentUsername, String targetUsername);

    void unfollow(String currentUsername, String targetUsername);

    boolean isFollowing(String currentUsername, String targetUsername);
}