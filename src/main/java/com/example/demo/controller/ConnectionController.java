package com.example.demo.controller;

import com.example.demo.service.ConnectionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/connection")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    // ================= SEND REQUEST =================

    @PostMapping("/send/{username}")
    public String sendRequest(@PathVariable String username,
                              Authentication authentication) {

        connectionService.sendRequest(
                authentication.getName(),
                username
        );

        return "redirect:/profile/" + username;
    }

    // ================= ACCEPT =================

    @PostMapping("/accept/{id}")
    public String acceptRequest(@PathVariable Long id,
                                Authentication authentication) {

        connectionService.acceptRequest(
                id,
                authentication.getName()
        );

        return "redirect:/connections";
    }

    // ================= REJECT =================

    @PostMapping("/reject/{id}")
    public String rejectRequest(@PathVariable Long id,
                                Authentication authentication) {

        connectionService.rejectRequest(
                id,
                authentication.getName()
        );

        return "redirect:/connections";
    }

    // ================= VIEW PENDING =================

    @GetMapping
    public String viewPendingRequests(Authentication authentication,
                                      Model model) {

        model.addAttribute("requests",
                connectionService.getPendingRequests(
                        authentication.getName()
                ));

        return "connections";
    }
}