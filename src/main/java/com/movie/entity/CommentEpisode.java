package com.movie.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    private Date timeAdd;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userAdd;

    @ManyToOne
    @JoinColumn(name = "episode_id", referencedColumnName = "id", nullable = false)
    private Episode episode;
}
