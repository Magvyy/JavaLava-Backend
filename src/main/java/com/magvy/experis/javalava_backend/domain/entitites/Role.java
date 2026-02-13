package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.domain.entitites.composite.RoleId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @EmbeddedId
    private RoleId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Role(User user, RoleEnum role) {
        this.id = new RoleId(user.getId(), role);
        this.user = user;
    }

    public Role() {

    }

    public RoleEnum getRole() {
        return this.id.getRole();
    }
}
