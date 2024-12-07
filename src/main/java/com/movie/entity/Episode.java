package com.movie.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "episodes_movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

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

    @Column(name = "file_episodes")
    private byte[] fileEpisodes;

    @Column(name = "subtitles")
    private byte[] subtitles;

    @Column(name = "views")
    private Long views;

    @Column(name = "likes")
    private Long likes;

    @ManyToOne
    @JoinColumn(name = "user_add")
    private User userAdd;

    @ManyToOne
    @JoinColumn(name = "user_update")
    private User userUpdate;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

}
