package com.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "time_add", nullable = false, updatable = false)
    private Date timeAdd;

    @Column(name = "time_update")
    private Date timeUpdate;

    @Column(name = "file_episodes", length = 10485760) // Tối đa 10MB
    private byte[] fileEpisodes;

    @Column(name = "subtitles", length = 10485760) // Tối đa 10MB
    private byte[] subtitles;

    @Column(name = "views", nullable = false)
    private Long views = 0L;

    @Column(name = "likes", nullable = false)
    private Long likes = 0L;

    @ManyToOne
    @JoinColumn(name = "user_add")
    private User userAdd;

    @ManyToOne
    @JoinColumn(name = "user_update")
    private User userUpdate;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEpisode> comments = new ArrayList<>();

    // Utility methods
    public void addComment(CommentEpisode comment) {
        comments.add(comment);
        comment.setEpisode(this);
    }

    public void removeComment(CommentEpisode comment) {
        comments.remove(comment);
        comment.setEpisode(null);
    }
}
