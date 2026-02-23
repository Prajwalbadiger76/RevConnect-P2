package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repo.ConnectionRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ConnectionServiceImpl(ConnectionRepository connectionRepository,
                                 UserRepository userRepository, NotificationService notificationService) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
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

        if (existing.isPresent()) {

            Connection connection = existing.get();

            if (connection.getStatus() == ConnectionStatus.PENDING) {
                return; // already pending
            }

            if (connection.getStatus() == ConnectionStatus.ACCEPTED) {
                return; // already connected
            }

            if (connection.getStatus() == ConnectionStatus.REJECTED) {
                connection.setStatus(ConnectionStatus.PENDING);
                connection.setCreatedAt(LocalDateTime.now());
                connectionRepository.save(connection);

                notificationService.createNotification(
                        receiver.getUsername(),
                        requester.getUsername(),
                        "CONNECTION_REQUEST",
                        null
                );
            }

            return;
        }

        Connection connection = new Connection();
        connection.setRequester(requester);
        connection.setReceiver(receiver);
        connection.setStatus(ConnectionStatus.PENDING);
        connection.setCreatedAt(LocalDateTime.now());

        connectionRepository.save(connection);
        
        System.out.println("Sending connection notification...");
        notificationService.createNotification(
                receiver.getUsername(),
                requester.getUsername(),
                "CONNECTION_REQUEST",
                null
        );
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
    
    @Override
    public List<User> getAcceptedConnections(String username) {

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow();

        List<Connection> sentAccepted =
                connectionRepository.findByRequesterAndStatus(
                        currentUser,
                        ConnectionStatus.ACCEPTED
                );

        List<Connection> receivedAccepted =
                connectionRepository.findByReceiverAndStatus(
                        currentUser,
                        ConnectionStatus.ACCEPTED
                );

        List<User> connectedUsers = new ArrayList<>();

        // If current user sent request
        for (Connection c : sentAccepted) {
            connectedUsers.add(c.getReceiver());
        }

        // If current user received request
        for (Connection c : receivedAccepted) {
            connectedUsers.add(c.getRequester());
        }

        return connectedUsers;
    }
}