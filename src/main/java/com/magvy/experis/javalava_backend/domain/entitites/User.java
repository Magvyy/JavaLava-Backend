    package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private int id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "age")
    private int age;

    @Column(name = "birthday")
    private Date birthday;


}
