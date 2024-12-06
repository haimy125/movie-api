package com.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

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
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "time_add")
    private Date timeAdd;

    @Column(name = "time_update")
    private Date timeUpdate;

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
