package com.example.demo.service;

import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.repo.FollowRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public ProfileServiceImpl(UserRepository userRepository,
                              FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    // ================= MY PROFILE =================

    @Override
    public ProfileResponse getMyProfile(String username) {
        return getProfileWithFollowInfo(username, username);
    }

    // ✅ REQUIRED BY INTERFACE (KEEP THIS)
    @Override
    public ProfileResponse getProfile(String username) {
        return getProfileWithFollowInfo(username, username);
    }

    // ================= VIEW PROFILE WITH FOLLOW INFO =================

    @Override
    public ProfileResponse getProfileWithFollowInfo(String currentUsername,
                                                    String targetUsername) {

        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();
        User targetUser = userRepository.findByUsername(targetUsername).orElseThrow();

        boolean isOwn = currentUsername.equals(targetUsername);

        boolean isFollowing = false;
        if (!isOwn) {
            isFollowing = followRepository
                    .findByFollowerAndFollowing(currentUser, targetUser)
                    .isPresent();
        }

        long followerCount =
                followRepository.countByFollowing(targetUser);

        long followingCount =
                followRepository.countByFollower(targetUser);

        return new ProfileResponse(
                targetUser.getUsername(),
                targetUser.getEmail(),
                targetUser.getRole(),
                targetUser.getFullName(),
                targetUser.getBio(),
                targetUser.getProfilePicture(),
                targetUser.getLocation(),
                targetUser.getWebsite(),
                targetUser.isPrivate(),
                isFollowing,
                followerCount,
                followingCount,
                isOwn
        );
    }

    // ================= UPDATE PROFILE =================

    @Override
    public ProfileResponse updateProfile(String username,
                                         UpdateProfileRequest request) {

        User user = findUser(username);

        if (request.fullName() != null)
            user.setFullName(request.fullName());

        if (request.bio() != null)
            user.setBio(request.bio());

        if (request.profilePicture() != null)
            user.setProfilePicture(request.profilePicture());

        if (request.location() != null)
            user.setLocation(request.location());

        if (request.website() != null)
            user.setWebsite(request.website());

        if (request.isPrivate() != null)
            user.setPrivate(request.isPrivate());

        userRepository.save(user);

        return getMyProfile(username);
    }

    // ================= SEARCH USERS =================

    @Override
    public List<ProfileResponse> searchUsers(String keyword) {

        return userRepository
                .findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                        keyword, keyword)
                .stream()
                .map(user -> new ProfileResponse(
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getFullName(),
                        user.getBio(),
                        user.getProfilePicture(),
                        user.getLocation(),
                        user.getWebsite(),
                        user.isPrivate(),
                        false,
                        followRepository.countByFollowing(user),
                        followRepository.countByFollower(user),
                        false
                ))
                .toList();
    }

    // ================= HELPER =================

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found"));
    }
}