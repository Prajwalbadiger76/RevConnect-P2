package com.example.demo.service;

import com.example.demo.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    void createNotification(String recipientUsername,
                            String senderUsername,
                            String type,
                            Long referenceId);

    List<NotificationDto> getUserNotifications(String username);

    long getUnreadCount(String username);

    void markAsRead(Long notificationId);
}