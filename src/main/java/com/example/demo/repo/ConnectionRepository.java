package com.example.demo.repo;

import com.example.demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository
        extends JpaRepository<Connection, Long> {

    Optional<Connection> findByRequesterAndReceiver(User requester, User receiver);

    List<Connection> findByReceiverAndStatus(User receiver, ConnectionStatus status);

    List<Connection> findByRequesterAndStatus(User requester, ConnectionStatus status);

    long countByRequesterAndStatus(User requester, ConnectionStatus status);

    long countByReceiverAndStatus(User receiver, ConnectionStatus status);
}