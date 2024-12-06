package com.movie.service.user;

import com.movie.dto.CommentEpisodeDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentEpisodeService {
    
    List<CommentEpisodeDTO> getByEpisode(Long episodeid, Pageable pageable);

    CommentEpisodeDTO save(CommentEpisodeDTO comment);

    int totalItem();
}
