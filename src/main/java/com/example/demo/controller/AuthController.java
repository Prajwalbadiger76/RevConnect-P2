package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
//
//        userService.register(request);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body("User registered successfully");
//    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request) {

        userService.register(request);

        return "redirect:/login";
    }
    

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(
//            @Valid @RequestBody LoginRequest request) {
//
//        String token = userService.login(request);
//
//        return ResponseEntity.ok(new LoginResponse(token));
//    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest request,
                        HttpServletResponse response) {

        String token = userService.login(request);

        Cookie jwtCookie = new Cookie("JWT", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60);

        response.addCookie(jwtCookie);

        return "redirect:/profile/me";
    }

}
