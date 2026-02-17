package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;

public interface UserService {

    void register(RegisterRequest request);

    String login(LoginRequest request);
}
