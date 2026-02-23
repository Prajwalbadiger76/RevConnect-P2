package com.example.demo.controller;

import com.example.demo.dto.NotificationDto;
import com.example.demo.service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationDto> getNotifications(Authentication auth) {
        return notificationService.getUserNotifications(auth.getName());
    }

    @GetMapping("/count")
    public long getUnreadCount(Authentication auth) {
        return notificationService.getUnreadCount(auth.getName());
    }

    @PostMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}