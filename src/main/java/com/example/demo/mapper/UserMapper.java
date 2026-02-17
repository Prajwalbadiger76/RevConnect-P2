package com.example.demo.mapper;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;

public class UserMapper {

    public static User toEntity(RegisterRequest request, String encodedPassword) {

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encodedPassword);
        user.setRole(request.role());

        return user;
    }
}
