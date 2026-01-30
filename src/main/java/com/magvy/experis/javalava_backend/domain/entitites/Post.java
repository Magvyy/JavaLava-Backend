package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Timestamp published;
    private boolean visible;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) // Use CascadeType.REMOVE or ALL
    private List<Comment> comments;

    public Post(Long id, String content, LocalDateTime published, boolean visible, User user) {
        this.id = id;
        this.content = content;
        this.published = Timestamp.valueOf(published);
        this.visible = visible;
        this.user = user;
    }

    public Post() {}
}
