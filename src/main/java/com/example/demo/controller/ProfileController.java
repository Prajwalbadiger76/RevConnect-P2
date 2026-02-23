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

    // ================= VIEW MY PROFILE =================
    @GetMapping
    public String getMyProfile(Authentication authentication, Model model) {

        ProfileResponse profile =
                profileService.getProfileWithFollowInfo(
                        authentication.getName(),
                        authentication.getName()
                );

        model.addAttribute("profile", profile);

        return "profile";
    }

    // ================= VIEW OTHER PROFILE =================
    @GetMapping("/{username}")
    public String viewProfile(@PathVariable String username,
                              Authentication authentication,
                              Model model) {

        ProfileResponse profile =
                profileService.getProfileWithFollowInfo(
                        authentication.getName(),
                        username
                );

        model.addAttribute("profile", profile);

        return "profile";
    }

    // ================= EDIT PROFILE PAGE =================
    @GetMapping("/edit")
    public String editProfilePage(Authentication authentication, Model model) {

        ProfileResponse profile =
                profileService.getMyProfile(authentication.getName());

        model.addAttribute("profile", profile);

        return "edit-profile";
    }

    // ================= UPDATE PROFILE =================
    @PostMapping("/edit")
    public String updateProfile(Authentication authentication,
                                @ModelAttribute UpdateProfileRequest request) {

        profileService.updateProfile(authentication.getName(), request);

        return "redirect:/profile";
    }

    // ================= SEARCH USERS =================
    @GetMapping("/search")
    public String searchUsers(@RequestParam String keyword,
                              Authentication authentication,
                              Model model) {

        model.addAttribute("results",
                profileService.searchUsers(keyword));

        model.addAttribute("currentUsername",
                authentication.getName());

        return "search-results";
    }
}