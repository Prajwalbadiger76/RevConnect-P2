package com.example.demo.service;

import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    public ProfileServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ================= MY PROFILE =================

    @Override
    public ProfileResponse getMyProfile(String username) {
        User user = findUser(username);
        return map(user);
    }

    // ================= VIEW OTHER PROFILE =================

    @Override
    public ProfileResponse getProfile(String username) {

        User user = findUser(username);

        if (user.isPrivate()) {
            throw new CustomException("This profile is private");
        }

        return map(user);
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

        return map(user);
    }

    // ================= SEARCH USERS =================

    @Override
    public List<ProfileResponse> searchUsers(String keyword) {

        return userRepository
                .findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                        keyword, keyword)
                .stream()
                .map(this::map)
                .toList();
    }

    // ================= HELPERS =================

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found"));
    }

    private ProfileResponse map(User user) {
        return new ProfileResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getFullName(),
                user.getBio(),
                user.getProfilePicture(),
                user.getLocation(),
                user.getWebsite(),
                user.isPrivate()
        );
    }
}