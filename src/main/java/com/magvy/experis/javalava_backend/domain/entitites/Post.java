package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "published", nullable = false)
    private LocalDateTime published;

    @Column(name = "visible", nullable = false)
    private boolean visible;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) // Use CascadeType.REMOVE or ALL
    private List<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) // Use CascadeType.REMOVE or ALL
    private List<Comment> comments;

    public Post(Long id, String content, boolean visible, User user) {
        this.id = id;
        this.content = content;
        this.published = LocalDateTime.now();
        this.visible = visible;
        this.user = user;
    }

    public Post(Long id, String content, LocalDateTime published, boolean visible, User user) {
        this.id = id;
        this.content = content;
        this.published = published;
        this.visible = visible;
        this.user = user;
    }

    public Post() {}
}
