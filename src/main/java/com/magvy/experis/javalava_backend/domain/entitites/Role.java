package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.domain.entitites.composite.RoleId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @EmbeddedId
    private RoleId id;

    public Role() {

    }

    public Role(User user, RoleEnum role) {
        this.id = new RoleId(user, role);
    }

    public RoleEnum getRole() {
        return id.getRole();
    }
}
