package com.example.demo.dto;

import com.example.demo.entity.Role;
import jakarta.validation.constraints.*;

public record RegisterRequest(

        @NotBlank String username,
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotNull Role role
) {}
