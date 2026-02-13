package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "sent", nullable = false)
    private LocalDateTime sent;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User to;


    public Message() {}

    public Message(String content, LocalDateTime sent, User from, User to) {
        this.content = content.trim();
        this.sent = sent;
        this.from = from;
        this.to = to;
    }

}
