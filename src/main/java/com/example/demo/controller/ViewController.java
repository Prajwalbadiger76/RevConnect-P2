package com.example.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // ===== Login Page =====
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ===== Register Page =====
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // ===== Optional: Home Page =====
    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/login";
    }
}

