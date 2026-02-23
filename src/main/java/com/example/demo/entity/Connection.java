package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "connections",
       uniqueConstraints = @UniqueConstraint(
               columnNames = {"requester_id", "receiver_id"}))
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "connection_seq")
    @SequenceGenerator(name = "connection_seq",
            sequenceName = "connection_seq",
            allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    private ConnectionStatus status;

    private LocalDateTime createdAt;

    public Connection() {}

    public Long getId() { return id; }

    public User getRequester() { return requester; }
    public void setRequester(User requester) { this.requester = requester; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }

    public ConnectionStatus getStatus() { return status; }
    public void setStatus(ConnectionStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}