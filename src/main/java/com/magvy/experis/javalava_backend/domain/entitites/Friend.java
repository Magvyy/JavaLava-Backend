    package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendId;
import com.magvy.experis.javalava_backend.domain.entitites.composite.LikeId;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
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
}
