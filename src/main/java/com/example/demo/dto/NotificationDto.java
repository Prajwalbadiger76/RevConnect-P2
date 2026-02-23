package com.example.demo.dto;

import java.time.LocalDateTime;

public class NotificationDto {

    private Long id;
    private String message;
    private boolean read;
    private Long referenceId;
    private LocalDateTime createdAt;

    public NotificationDto(Long id, String message,
                           boolean read, Long referenceId,
                           LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.read = read;
        this.referenceId = referenceId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getMessage() { return message; }
    public boolean isRead() { return read; }
    public Long getReferenceId() { return referenceId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}