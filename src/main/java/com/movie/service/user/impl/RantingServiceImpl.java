package com.movie.service.user.impl;

import com.movie.dto.UserRatingDTO;
import com.movie.entity.Movie;
import com.movie.entity.User;
import com.movie.entity.UserRating;
import com.movie.repository.admin.MovieRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.repository.user.RatingRepository;
import com.movie.service.user.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RantingServiceImpl implements RatingService {
    
    @Autowired
    private RatingRepository ratings_Repository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<UserRatingDTO> getByUser(Long userid, Pageable pageable) {
        return List.of();
    }

    @Override
    public List<UserRatingDTO> getByMovie(Long movieid, Pageable pageable) {
        return List.of();
    }

    @Override
    public UserRatingDTO getByMovieAndUser(Long movieid, Long userid) {
        try {
            if (movieid == null)
                throw new RuntimeException("Bạn chưa nhập id phim");
            if (userid == null)
                throw new RuntimeException(" bạn chọn id người dùng");
            User user = userRepository.findById(userid).orElseThrow(() -> new RuntimeException("Không có người dùng có id này"));
            Movie movie = movieRepository.findById(movieid).orElseThrow(() -> new RuntimeException("Không có phim có id này"));
            UserRating ratingsEntity = ratings_Repository.findByUserAndMovie(user, movie);
            if (ratingsEntity == null)
                throw new RuntimeException("Không có đánh giá nào");
            UserRatingDTO dto = modelMapper.map(ratingsEntity, UserRatingDTO.class);
            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Không có đánh giá nào");
        }
    }

    @Override
    public void save(UserRatingDTO ratings) {
        if (ratings == null)
            throw new RuntimeException("Bạn chưa đánh giá phim");
        User user = userRepository.findById(Long.valueOf(ratings.getUser().getId())).orElseThrow(() -> new RuntimeException("Không có người dùng có id này"));
        Movie movie = movieRepository.findById(ratings.getMovie().getId()).orElseThrow(() -> new RuntimeException("Không có phim có id này"));
        UserRating ratingsEntity = new UserRating();
        ratingsEntity.setUser(user);
        ratingsEntity.setMovie(movie);
        ratingsEntity.setRating(ratings.getRating());
        ratings_Repository.save(ratingsEntity);
    }
}
