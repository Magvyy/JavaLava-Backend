package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "friends")
public class Friend {

    @EmbeddedId
    private FriendId id;

    @MapsId("user1")
    @ManyToOne
    @JoinColumn(name = "user_id_1")
    private User user1;

    @MapsId("user2")
    @ManyToOne
    @JoinColumn(name = "user_id_2")
    private User user2;

    public Friend() {}

    public Friend(User a, User b) {
        if (a.getId() < b.getId()) {
            this.user1 = a;
            this.user2 = b;
        } else {
            this.user1 = b;
            this.user2 = a;
        }
        this.id = new FriendId(this.user1.getId(), this.user2.getId());
    }
}
