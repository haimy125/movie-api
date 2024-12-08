package com.movie.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment_episodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEpisode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "time_add", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime timeAdd;

    // Phương thức tự động gán thời gian trước khi lưu
    @PrePersist
    protected void onCreate() {
        this.timeAdd = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userAdd;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    private Episode episode;

}
