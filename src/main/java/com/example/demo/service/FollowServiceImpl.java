package com.example.demo.service;

import com.example.demo.entity.Follow;
import com.example.demo.entity.User;
import com.example.demo.repo.FollowRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowServiceImpl(FollowRepository followRepository,
                             UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void follow(String currentUsername, String targetUsername) {

        if (currentUsername.equals(targetUsername)) return;

        User follower = userRepository.findByUsername(currentUsername).orElseThrow();
        User following = userRepository.findByUsername(targetUsername).orElseThrow();

        if (followRepository.findByFollowerAndFollowing(follower, following).isPresent()) {
            return;
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);
    }

    @Override
    public void unfollow(String currentUsername, String targetUsername) {

        User follower = userRepository.findByUsername(currentUsername).orElseThrow();
        User following = userRepository.findByUsername(targetUsername).orElseThrow();

        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    @Override
    public boolean isFollowing(String currentUsername, String targetUsername) {

        User follower = userRepository.findByUsername(currentUsername).orElseThrow();
        User following = userRepository.findByUsername(targetUsername).orElseThrow();

        return followRepository
                .findByFollowerAndFollowing(follower, following)
                .isPresent();
    }
}