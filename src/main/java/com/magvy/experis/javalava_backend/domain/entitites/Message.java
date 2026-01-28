package com.magvy.experis.javalava_backend.domain.entitites;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Date;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private User to;


    public Message() {}

    public Message(String content, Date date, User from, User to) {
        this.content = content.trim();
        this.date = date;
        this.from = from;
        this.to = to;
    }

}
