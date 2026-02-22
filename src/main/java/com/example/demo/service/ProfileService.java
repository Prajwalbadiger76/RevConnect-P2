package com.example.demo.service;

import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.UpdateProfileRequest;

import java.util.List;

public interface ProfileService {

    ProfileResponse getMyProfile(String username);

    ProfileResponse getProfile(String username);

    ProfileResponse updateProfile(String username, UpdateProfileRequest request);

    List<ProfileResponse> searchUsers(String keyword);
    
    ProfileResponse getProfileWithFollowInfo(String currentUsername, String targetUsername);
    
}
