package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repo.ConnectionRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public ConnectionServiceImpl(ConnectionRepository connectionRepository,
                                 UserRepository userRepository) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
    }

    // ================= SEND REQUEST =================

    @Override
    public void sendRequest(String currentUsername, String targetUsername) {

        if (currentUsername.equals(targetUsername)) return;

        User requester = userRepository.findByUsername(currentUsername)
                .orElseThrow();

        User receiver = userRepository.findByUsername(targetUsername)
                .orElseThrow();

        Optional<Connection> existing =
                connectionRepository.findByRequesterAndReceiver(requester, receiver);

        if (existing.isPresent()) return;

        Connection connection = new Connection();
        connection.setRequester(requester);
        connection.setReceiver(receiver);
        connection.setStatus(ConnectionStatus.PENDING);
        connection.setCreatedAt(LocalDateTime.now());

        connectionRepository.save(connection);
    }

    // ================= ACCEPT REQUEST =================

    @Override
    public void acceptRequest(Long connectionId, String currentUsername) {

        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow();

        if (!connection.getReceiver().getUsername().equals(currentUsername)) {
            throw new RuntimeException("Unauthorized action");
        }

        connection.setStatus(ConnectionStatus.ACCEPTED);
        connectionRepository.save(connection);
    }

    // ================= REJECT REQUEST =================

    @Override
    public void rejectRequest(Long connectionId, String currentUsername) {

        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow();

        if (!connection.getReceiver().getUsername().equals(currentUsername)) {
            throw new RuntimeException("Unauthorized action");
        }

        connection.setStatus(ConnectionStatus.REJECTED);
        connectionRepository.save(connection);
    }

    // ================= CHECK STATUS =================

    @Override
    public String getConnectionStatus(String currentUsername, String targetUsername) {

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow();

        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow();

        // Check request sent
        Optional<Connection> sent =
                connectionRepository.findByRequesterAndReceiver(currentUser, targetUser);

        if (sent.isPresent()) {
            return sent.get().getStatus().name();
        }

        // Check request received
        Optional<Connection> received =
                connectionRepository.findByRequesterAndReceiver(targetUser, currentUser);

        if (received.isPresent()) {
            return received.get().getStatus().name();
        }

        return "NONE";
    }

    // ================= COUNT CONNECTIONS =================

    @Override
    public long getConnectionCount(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        long asRequester =
                connectionRepository.countByRequesterAndStatus(user, ConnectionStatus.ACCEPTED);

        long asReceiver =
                connectionRepository.countByReceiverAndStatus(user, ConnectionStatus.ACCEPTED);

        return asRequester + asReceiver;
    }

    // ================= GET PENDING REQUESTS =================

    @Override
    public List<Connection> getPendingRequests(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        return connectionRepository
                .findByReceiverAndStatus(user, ConnectionStatus.PENDING);
    }
    @Override
    public Long getPendingRequestId(String currentUsername, String targetUsername) {

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow();

        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow();

        Optional<Connection> received =
                connectionRepository.findByRequesterAndReceiver(
                        targetUser,
                        currentUser
                );

        if (received.isPresent()
                && received.get().getStatus() == ConnectionStatus.PENDING) {

            return received.get().getId();
        }

        return null;
    }
}