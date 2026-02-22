package com.example.demo.service;

import com.example.demo.dto.PostDto;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository,
                           HashtagRepository hashtagRepository,
                           PostHashtagRepository postHashtagRepository,
                           UserRepository userRepository) {
        this.postRepository = postRepository;
        this.hashtagRepository = hashtagRepository;
        this.postHashtagRepository = postHashtagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createPost(String username, String content) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        Post post = new Post();
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);

        postRepository.save(post);

        // 🔥 Hashtag Parsing
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {

            String tag = matcher.group(1).toLowerCase();

            Hashtag hashtag = hashtagRepository
                    .findByName(tag)
                    .orElseGet(() -> {
                        Hashtag newTag = new Hashtag();
                        newTag.setName(tag);
                        return hashtagRepository.save(newTag);
                    });

            PostHashtag ph = new PostHashtag();
            ph.setPost(post);
            ph.setHashtag(hashtag);

            postHashtagRepository.save(ph);
        }
    }

    @Override
    public List<PostDto> getUserPosts(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        return postRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAllPosts() {

        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private PostDto map(Post post) {

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUsername(post.getUser().getUsername());

        return dto;
    }
}