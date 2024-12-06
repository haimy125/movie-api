package com.movie.repository.user;

import com.movie.entity.CommentEpisode;
import com.movie.entity.Episode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentEpisodeRepository extends JpaRepository<CommentEpisode, Long> {
    List<CommentEpisode> findByEpisode(Episode episodeId, Pageable pageable);

    List<CommentEpisode> findByEpisode(Episode episodeId);
}
