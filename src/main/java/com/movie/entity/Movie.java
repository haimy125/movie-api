package com.movie.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vn_name")
    private String vnName;

    @Column(name = "cn_name")
    private String cnName;

    @Column(name = "description")
    private String description;

    @Lob // Đảm bảo sử dụng annotation @Lob cho dữ liệu lớn như byte[]
    @Column(name = "image_url")
    private byte[] imageUrl;

    @Column(name = "image_base64", length = Integer.MAX_VALUE)
    private String imageBase64;

    @Column(name = "time_add", nullable = false, updatable = false)
    private Date timeAdd;

    @Column(name = "time_update")
    private Date timeUpdate;

    @Column(name = "author")
    private String author;

    @Column(name = "episode_number")
    private Long episodeNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "new_movie")
    private Boolean newMovie;

    @Column(name = "hot_movie")
    private Boolean hotMovie;

    @Column(name = "vip_movie")
    private Boolean vipMovie;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "year")
    private Long year;

    @Column(name = "total_views")
    private Long totalViews;

    @ManyToOne
    @JoinColumn(name = "user_add")
    private User userAdd;

    @ManyToOne
    @JoinColumn(name = "user_update")
    private User userUpdate;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<ScheduleMovie> scheduleMovies;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryMovie> categoryMovies = new ArrayList<>();
}
