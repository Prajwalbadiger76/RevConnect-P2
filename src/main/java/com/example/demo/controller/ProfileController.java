package com.example.demo.controller;

import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.service.ProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ProfileResponse getMyProfile(Authentication authentication) {
        return profileService.getMyProfile(authentication.getName());
    }

    @PutMapping("/me")
    public ProfileResponse updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {

        return profileService.updateProfile(authentication.getName(), request);
    }

    @GetMapping("/{username}")
    public ProfileResponse getProfile(@PathVariable String username) {
        return profileService.getProfile(username);
    }

    @GetMapping("/search")
    public List<ProfileResponse> search(@RequestParam String keyword) {
        return profileService.searchUsers(keyword);
    }
    @GetMapping("/debug")
    public String debug(Authentication authentication) {
        return "User: " + authentication.getName() +
               " | Authorities: " + authentication.getAuthorities();
    }

}
