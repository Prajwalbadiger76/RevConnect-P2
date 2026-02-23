package com.example.demo.repo;

import com.example.demo.entity.Connection;
import com.example.demo.entity.ConnectionStatus;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository
        extends JpaRepository<Connection, Long> {

    // Check exact request (A -> B)
    Optional<Connection> findByRequesterAndReceiver(
            User requester,
            User receiver
    );

    // Get pending requests received by user
    List<Connection> findByReceiverAndStatus(
            User receiver,
            ConnectionStatus status
    );

    // Get requests sent by user
    List<Connection> findByRequesterAndStatus(
            User requester,
            ConnectionStatus status
    );

    // Count accepted connections (as requester)
    long countByRequesterAndStatus(
            User requester,
            ConnectionStatus status
    );

    // Count accepted connections (as receiver)
    long countByReceiverAndStatus(
            User receiver,
            ConnectionStatus status
    );
}