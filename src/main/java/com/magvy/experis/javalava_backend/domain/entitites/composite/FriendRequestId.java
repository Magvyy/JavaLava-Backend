package com.magvy.experis.javalava_backend.domain.entitites.composite;

import com.magvy.experis.javalava_backend.domain.entitites.User;

import java.io.Serializable;
import java.util.Objects;

public class FriendRequestId implements Serializable {
    private Long from; // user id
    private Long to;   // user id

    public FriendRequestId() {}

    public FriendRequestId(Long from, Long to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendRequestId that)) return false;
        return from.equals(that.from) && to.equals(that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
