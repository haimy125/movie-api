package com.movie.service.admin;

import com.movie.dto.MovieDTO;
import com.movie.dto.UserMovieDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public interface MovieService {

    List<MovieDTO> getAll(Pageable pageable);

    List<MovieDTO> getByNew_movie(Boolean new_movie, Pageable pageable);

    List<MovieDTO> getByHot_movie(Boolean hot_movie, Pageable pageable);

    List<MovieDTO> getByVip_movie(Boolean vip_movie, Pageable pageable);

    List<MovieDTO> getByAuthor(String author, Pageable pageable);

    List<MovieDTO> getByVn_name(String vn_name, Pageable pageable);

    List<MovieDTO> getByCn_name(String cn_name, Pageable pageable);

    List<MovieDTO> getByUser_add(Long user_add, Pageable pageable);

    List<MovieDTO> getByUser_update(Long user_update, Pageable pageable);

    List<MovieDTO> getByTime_add(Date time_add, Pageable pageable);

    List<MovieDTO> getByTime_update(Date time_update, Pageable pageable);

    MovieDTO getById(Long id);

    MovieDTO getByMovieId(Long id);

    MovieDTO create(MovieDTO MovieDTO, MultipartFile file, String categorylist, String scheduleList) throws IOException;

    MovieDTO update(MovieDTO MovieDTO, MultipartFile file, String categorylist, String scheduleList) throws IOException;

    UserMovieDetail getDetail(Long userId, Long movieId);

    void delete(Long id);

    int totalItem();
}
