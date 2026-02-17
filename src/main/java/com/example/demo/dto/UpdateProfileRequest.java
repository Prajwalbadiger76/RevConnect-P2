package com.example.demo.dto;

public record UpdateProfileRequest(
        String fullName,
        String bio,
        String profilePicture,
        String location,
        String website,
        Boolean isPrivate
) {}
