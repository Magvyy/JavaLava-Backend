package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendRequestId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "friend_requests")
public class FriendRequest {

    @EmbeddedId
    private FriendRequestId id;

    @MapsId("from")
    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User from;

    @MapsId("to")
    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User to;

    public FriendRequest() {}

    public FriendRequest(User from, User to) {
        this.from = from;
        this.to = to;
        this.id = new FriendRequestId(from.getId(), to.getId());
    }
}