package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hashtags")
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "hashtag_seq")
    @SequenceGenerator(name = "hashtag_seq",
            sequenceName = "hashtag_seq",
            allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Hashtag() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}