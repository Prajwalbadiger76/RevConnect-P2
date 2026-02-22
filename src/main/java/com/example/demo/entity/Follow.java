package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "follows",
       uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "follow_seq")
    @SequenceGenerator(name = "follow_seq",
            sequenceName = "follow_seq",
            allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    public Follow() {}

    public Long getId() { return id; }

    public User getFollower() { return follower; }
    public void setFollower(User follower) { this.follower = follower; }

    public User getFollowing() { return following; }
    public void setFollowing(User following) { this.following = following; }
}