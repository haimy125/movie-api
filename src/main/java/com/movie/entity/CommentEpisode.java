package com.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

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

    @Column(name = "time_add")
    private Date timeAdd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userAdd;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    private Episode episode;
}
