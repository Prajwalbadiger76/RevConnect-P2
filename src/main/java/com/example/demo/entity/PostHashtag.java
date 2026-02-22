package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_hashtags")
public class PostHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "post_hashtag_seq")
    @SequenceGenerator(name = "post_hashtag_seq",
            sequenceName = "post_hashtag_seq",
            allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public PostHashtag() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Post getPost() { return post; }

    public void setPost(Post post) { this.post = post; }

    public Hashtag getHashtag() { return hashtag; }

    public void setHashtag(Hashtag hashtag) { this.hashtag = hashtag; }
}