package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "published", nullable = false)
    private LocalDateTime published;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Comment(String content, Post post, User user) {
        this.content = content;
        this.published = LocalDateTime.now();
        this.post = post;
        this.user = user;
    }

    public Comment(String content, LocalDateTime published, Post post, User user) {
        this.content = content;
        this.published = published;
        this.post = post;
        this.user = user;
    }

    public Comment() {}
}
