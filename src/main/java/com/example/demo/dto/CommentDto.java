package com.example.demo.dto;

import java.time.LocalDateTime;

public class CommentDto {

    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private boolean ownedByCurrentUser;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isOwnedByCurrentUser() { return ownedByCurrentUser; }
    public void setOwnedByCurrentUser(boolean ownedByCurrentUser) {
        this.ownedByCurrentUser = ownedByCurrentUser;
    }
}