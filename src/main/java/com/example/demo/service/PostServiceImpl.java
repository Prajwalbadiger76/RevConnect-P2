package com.example.demo.service;

import com.example.demo.dto.CommentDto;
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
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public PostServiceImpl(PostRepository postRepository,
                           HashtagRepository hashtagRepository,
                           PostHashtagRepository postHashtagRepository,
                           UserRepository userRepository,
                           FollowRepository followRepository,
                           LikeRepository likeRepository,
                           CommentRepository commentRepository) {

        this.postRepository = postRepository;
        this.hashtagRepository = hashtagRepository;
        this.postHashtagRepository = postHashtagRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    // ================= CREATE POST =================

    @Override
    public void createPost(String username, String content) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        Post post = new Post();
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);

        postRepository.save(post);

        // Hashtag Parsing
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

    // ================= USER POSTS =================

    @Override
    public List<PostDto> getUserPosts(String username) {

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow();

        return postRepository.findByUserOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(post -> map(post, currentUser))
                .collect(Collectors.toList());
    }

    // ================= GLOBAL POSTS =================

    @Override
    public List<PostDto> getAllPosts() {

        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(post -> {
                    PostDto dto = new PostDto();
                    dto.setId(post.getId());
                    dto.setContent(post.getContent());
                    dto.setCreatedAt(post.getCreatedAt());
                    dto.setUsername(post.getUser().getUsername());
                    dto.setLikeCount(likeRepository.countByPost(post));
                    dto.setLikedByCurrentUser(false);
                    dto.setComments(Collections.emptyList());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ================= PERSONALIZED FEED =================

    @Override
    public List<PostDto> getFeedPosts(String username) {

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow();

        List<User> followedUsers = followRepository
                .findByFollower(currentUser)
                .stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        List<User> feedUsers = new ArrayList<>(followedUsers);
        feedUsers.add(currentUser);

        return postRepository
                .findByUserInOrderByCreatedAtDesc(feedUsers)
                .stream()
                .map(post -> map(post, currentUser))
                .collect(Collectors.toList());
    }

    // ================= DTO MAPPER =================

    private PostDto map(Post post, User currentUser) {

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUsername(post.getUser().getUsername());

        // Likes
        dto.setLikeCount(likeRepository.countByPost(post));
        dto.setLikedByCurrentUser(
                likeRepository.findByUserAndPost(currentUser, post).isPresent()
        );

        // Comments
        List<CommentDto> commentDtos = commentRepository
                .findByPostOrderByCreatedAtAsc(post)
                .stream()
                .map(comment -> {
                    CommentDto cd = new CommentDto();
                    cd.setId(comment.getId());
                    cd.setUsername(comment.getUser().getUsername());
                    cd.setContent(comment.getContent());
                    cd.setCreatedAt(comment.getCreatedAt());
                    cd.setOwnedByCurrentUser(
                            comment.getUser().getUsername()
                                    .equals(currentUser.getUsername())
                    );
                    return cd;
                })
                .collect(Collectors.toList());

        dto.setComments(commentDtos);

        return dto;
    }
}