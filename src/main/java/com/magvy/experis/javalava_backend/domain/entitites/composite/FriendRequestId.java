package com.magvy.experis.javalava_backend.domain.entitites.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class FriendRequestId implements Serializable {
    @Column(name = "from_user_id", nullable = false)
    private Long from; // user id

    @Column(name = "to_user_id", nullable = false)
    private Long to;   // user id

    public FriendRequestId(Long from, Long to) {
        this.from = from;
        this.to = to;
    }

    public FriendRequestId() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof FriendRequestId that)) return false;
        return from.equals(that.from) && to.equals(that.to) || from.equals(that.to) && to.equals(that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
