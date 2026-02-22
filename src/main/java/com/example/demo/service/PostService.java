package com.example.demo.service;

import com.example.demo.dto.PostDto;
import java.util.List;

public interface PostService {

    void createPost(String username, String content);

    List<PostDto> getUserPosts(String username);

    List<PostDto> getAllPosts();

    List<PostDto> getFeedPosts(String username);
}