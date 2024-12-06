package com.movie.service.user.impl;

import com.movie.dto.CommentMovieDTO;
import com.movie.entity.CommentMovie;
import com.movie.entity.Movie;
import com.movie.entity.User;
import com.movie.repository.admin.MovieRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.repository.user.CommentMovieRepository;
import com.movie.service.user.CommentMovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentMovieServiceImpl implements CommentMovieService {
    
    @Autowired
    private CommentMovieRepository comment_MovieRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<CommentMovieDTO> getByMovie(Long movieid, Pageable pageable) {
        List<CommentMovieDTO> UserDTOS = new ArrayList<>();
        // tìm kiếm tất cả người dùng
        Movie movie = movieRepository.findById(movieid).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        List<CommentMovie> list = comment_MovieRepository.findByMovie(movie, pageable);
        // chuyển đổi sang DTO
        for (CommentMovie item : list) {
            CommentMovieDTO commentMovieDto = modelMapper.map(item, CommentMovieDTO.class);
            UserDTOS.add(commentMovieDto);
        }
        return UserDTOS;
    }

    @Override
    public CommentMovieDTO save(CommentMovieDTO comment) {
        try {
            if (comment == null)
                throw new RuntimeException("Không tìm thấy người dùng");
            User user = userRepository.findById(Long.valueOf(comment.getUserAdd().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            Movie movie = movieRepository.findById(comment.getMovie().getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            CommentMovie comment_Movie = modelMapper.map(comment, CommentMovie.class);
            comment_Movie.setUserAdd(user);
            comment_Movie.setMovie(movie);
            comment_Movie.setTimeAdd(Date.valueOf(LocalDate.now()));
            comment_Movie.setContent(comment.getContent());
            comment_MovieRepository.save(comment_Movie);
            return comment;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu");
        }
    }

    @Override
    public int totalItem() {
        return (int) comment_MovieRepository.count();
    }
}
