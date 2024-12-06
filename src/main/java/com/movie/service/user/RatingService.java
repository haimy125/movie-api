package com.movie.service.user;

import com.movie.dto.UserRatingDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RatingService {
    
    List<UserRatingDTO> getByUser(Long userid, Pageable pageable);

    List<UserRatingDTO> getByMovie(Long movieid, Pageable pageable);

    UserRatingDTO getByMovieAndUser(Long movieid, Long userid);

    void save(UserRatingDTO ratings);
}
