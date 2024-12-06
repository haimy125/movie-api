package com.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "time_add")
    private Date timeAdd;

    @Column(name = "point")
    private Long point;

    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "fullname")
    private String fullname;

    @ManyToOne
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "FK_Users_Role"))
    private Role role;
}
