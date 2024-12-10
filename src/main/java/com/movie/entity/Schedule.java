package com.movie.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "time_add", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime timeAdd;

    // Phương thức tự động gán thời gian trước khi lưu
    @PrePersist
    protected void onCreate() {
        this.timeAdd = LocalDateTime.now();
    }

    @Column(name = "time_update")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timeUpdate;

    @PreUpdate
    private void updateTimeUpdate() {
        this.timeUpdate = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_add_id")
    private User userAdd;

    @ManyToOne
    @JoinColumn(name = "user_update_id")
    private User userUpdate;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ScheduleMovie> scheduleMovies;

}
