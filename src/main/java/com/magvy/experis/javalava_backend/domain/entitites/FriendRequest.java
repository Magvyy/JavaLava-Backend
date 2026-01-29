package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.domain.entitites.composite.FriendRequestId;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@IdClass(FriendRequestId.class)
@Table(name = "friend_requests")
public class FriendRequest {
    @Id
    @ManyToOne
    @JoinColumn(name = "from_id")
    private User from;

    @Id
    @ManyToOne
    @JoinColumn(name = "to_id")
    private User to;
}
