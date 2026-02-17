package com.example.demo.dto;

import com.example.demo.entity.Role;

public record ProfileResponse(
        String username,
        String email,
        Role role,
        String fullName,
        String bio,
        String profilePicture,
        String location,
        String website,
        boolean isPrivate
) {}
