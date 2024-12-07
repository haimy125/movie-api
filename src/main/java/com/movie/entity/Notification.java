package com.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "content")
    private String content;

    @Column(name = "time_add")
    private Date timeAdd;

    @Column(name = "time_update")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timeUpdate;

    @PreUpdate
    private void updateTimeUpdate() {
        this.timeUpdate = LocalDateTime.now();
    }

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
