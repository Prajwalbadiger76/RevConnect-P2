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

    // ✅ View My Profile Page
    @GetMapping("/me")
    public String getMyProfile(Authentication authentication, Model model) {

        ProfileResponse profile =
                profileService.getMyProfile(authentication.getName());

        model.addAttribute("profile", profile);

        return "profile";  // profile.html
    }

    // ✅ Show Edit Profile Page
    @GetMapping("/edit")
    public String editProfilePage(Authentication authentication, Model model) {

        ProfileResponse profile =
                profileService.getMyProfile(authentication.getName());

        model.addAttribute("profile", profile);

        return "edit-profile"; // edit-profile.html
    }

    // ✅ Handle Edit Profile Form Submit
    @PostMapping("/edit")
    public String updateProfile(Authentication authentication,
                                @ModelAttribute UpdateProfileRequest request) {

        profileService.updateProfile(authentication.getName(), request);

        return "redirect:/profile/me";
    }

    // ✅ View Other User Profile
    @GetMapping("/{username}")
    public String getProfile(@PathVariable String username, Model model) {

        ProfileResponse profile =
                profileService.getProfile(username);

        model.addAttribute("profile", profile);

        return "profile";
    }

    // ✅ Search Users Page
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {

        model.addAttribute("users",
                profileService.searchUsers(keyword));

        return "search-results"; // search-results.html
    }
}
