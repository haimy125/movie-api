package com.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId; ;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
}
