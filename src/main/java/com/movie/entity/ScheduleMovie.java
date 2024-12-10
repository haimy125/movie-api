package com.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "schedule_movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false) // Cột khóa ngoại movie_id trong bảng schedule_movies
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id", nullable = false) // Cột khóa ngoại schedule_id trong bảng schedule_movies
    private Schedule schedule;
}
