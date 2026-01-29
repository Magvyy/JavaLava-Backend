package com.magvy.experis.javalava_backend.domain.entitites.composite;

import java.io.Serializable;
import java.util.Objects;

public class FriendId implements Serializable {
    private Long user1;
    private Long user2;

    public FriendId() {}

    public FriendId(Long user1, Long user2) {
        if (user1 < user2) {
            this.user1 = user1;
            this.user2 = user2;
        } else {
            this.user1 = user2;
            this.user2 = user1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendId that)) return false;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }
}
