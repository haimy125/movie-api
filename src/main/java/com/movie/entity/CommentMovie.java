package com.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "CommentMovies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", columnDefinition = "Nvarchar(max)")
    private String content;

    @Column(name = "time_add")
    private Date timeAdd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userAdd;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
