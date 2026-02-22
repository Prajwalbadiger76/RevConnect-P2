package com.example.demo.controller;

import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.service.ProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // View Profile
    @GetMapping("/me")
    public String getMyProfile(Authentication authentication, Model model) {

        ProfileResponse profile =
                profileService.getMyProfile(authentication.getName());

        model.addAttribute("profile", profile);

        return "profile";
    }

    // Show Edit Page
    @GetMapping("/edit")
    public String editProfilePage(Authentication authentication, Model model) {

        ProfileResponse profile =
                profileService.getMyProfile(authentication.getName());

        model.addAttribute("profile", profile);

        return "edit-profile";
    }

    // Handle Edit Submit
    @PostMapping("/edit")
    public String updateProfile(Authentication authentication,
                                @ModelAttribute UpdateProfileRequest request) {

        profileService.updateProfile(authentication.getName(), request);

        return "redirect:/profile/me";
    }
}
