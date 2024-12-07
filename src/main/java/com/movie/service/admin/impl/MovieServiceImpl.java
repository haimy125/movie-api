package com.movie.service.admin.impl;

import com.movie.dto.MovieDTO;
import com.movie.entity.*;
import com.movie.repository.admin.*;
import com.movie.service.admin.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMovieRepository categoryMovieRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private ScheduleMovieRepository scheduleMovieRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<MovieDTO> getAll(Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> Movie = movieRepository.findAll(pageable).getContent();
        if (Movie == null) throw new RuntimeException("Không có bộ phim nào");
        for (Movie movie : Movie) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByNew_movie(Boolean newMovie, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> Movie = movieRepository.findByNewMovie(newMovie, pageable);
        if (Movie == null) throw new RuntimeException("Không có bộ phim mới nào");
        for (Movie movie : Movie) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
//            dto.setImageUrl(Base64.getEncoder().encodeToString(movie.getImageUrl()).getBytes());
            dto.setImageUrl(Base64.getEncoder().encode(movie.getImageUrl().getBytes()));
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByHot_movie(Boolean hotMovie, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> Movie = movieRepository.findByHotMovie(hotMovie, pageable);
        if (Movie == null) throw new RuntimeException("Không có bộ phim hot nào");
        for (Movie movie : Movie) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(Base64.getEncoder().encode(movie.getImageUrl().getBytes()));
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByVip_movie(Boolean vipMovie, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> movies = movieRepository.findByVipMovie(vipMovie, pageable);
        if (movies == null) throw new RuntimeException("Không có bộ phim trả phí nào");
        for (Movie movie : movies) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(Base64.getEncoder().encode(movie.getImageUrl().getBytes()));
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByAuthor(String author, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> movies = movieRepository.findByAuthorLike("%" + author + "%", pageable);
        if (movies == null) throw new RuntimeException("Không có bộ phim trả phí nào");
        for (Movie movie : movies) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(Base64.getEncoder().encode(movie.getImageUrl().getBytes()));
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByVn_name(String vnName, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> Movie = movieRepository.findByVnNameLike("%" + vnName + "%", pageable);
        if (Movie == null) throw new RuntimeException("Không có bộ phim trả phí nào");
        for (Movie movie : Movie) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByCn_name(String cnName, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> movies = movieRepository.findByCnNameLike("%" + cnName + "%", pageable);
        if (movies == null) throw new RuntimeException("Không có bộ phim có tên này");
        for (Movie movie : movies) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByUser_add(Long userAdd, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        User User = userRepository.findById(userAdd).orElseThrow(() -> new RuntimeException("Không có người dùng này"));
        List<Movie> movies = movieRepository.findByUserAdd(User, pageable);
        if (movies == null) throw new RuntimeException("Không có bộ phim nào");
        for (Movie movie : movies) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByUser_update(Long userUpdate, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        User User = userRepository.findById(userUpdate).orElseThrow(() -> new RuntimeException("Không có người dùng này"));
        List<Movie> movies = movieRepository.findByUserUpdate(User, pageable);
        if (movies == null) throw new RuntimeException("Không có bộ phim nào");
        for (Movie movie : movies) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByTime_add(Date timeAdd, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> movies = movieRepository.findByTimeAdd(timeAdd, pageable);
        if (movies == null) throw new RuntimeException("Không có bộ phim có tên này");
        for (Movie movie : movies) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByTime_update(Date timeUpdate, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> movies = movieRepository.findByTimeUpdate(timeUpdate, pageable);
        if (movies == null) throw new RuntimeException("Không có bộ phim có tên này");
        for (Movie movie : movies) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            result.add(dto);
        }
        return result;
    }

    @Override
    public MovieDTO getById(Long id) {
        try {
            Movie Movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có bộ phim này"));
            MovieDTO dto = modelMapper.map(Movie, MovieDTO.class);
            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy bộ phim");
        }
    }

    @Override
    public MovieDTO getByMovieId(Long id) {
        try {
            Movie Movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có bộ phim này"));
            List<Episode> Episode = episodeRepository.findByMovie(Movie);
            if (Episode == null) Movie.setTotalViews(0L);
            else {

                long totalviews = 0;
                for (Episode episode : Episode) {
                    totalviews += episode.getViews();
                }
                if (totalviews == Movie.getTotalViews()) {
                    Movie.setTotalViews(totalviews);
                    movieRepository.save(Movie);
                }
            }

            MovieDTO dto = modelMapper.map(Movie, MovieDTO.class);
            dto.setImageUrl(null);
            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy bộ phim");
        }
    }

    @Override
    public MovieDTO create(MovieDTO movieDTO, MultipartFile file, String categorylist, String scheduleList) throws IOException {
        try {
            if (movieDTO == null) {
                throw new RuntimeException("Bạn chưa nhập dữ liệu!");
            }

            if (file == null) {
                throw new RuntimeException("Vui lòng chọn ảnh của bộ phim!");
            }

            if (categorylist == null || categorylist.isEmpty()) {
                throw new RuntimeException("Vui lòng chọn thể loại của bộ phim!");
            }

            // Xử lý danh sách thể loại
            List<String> categoryIds = Arrays.asList(categorylist.split(","));
            List<Category> categoryEntities = new ArrayList<>();

            for (String idStr : categoryIds) {
//                Long id = Long.valueOf(idStr.trim()); // Chuyển đổi và loại bỏ khoảng trắng
                Category category = new Category();
                category.setId(idStr);
                categoryEntities.add(category);
            }
            // Xử lý danh sách thể loại
            List<String> scheduleIds = Arrays.asList(scheduleList.split(","));
            List<Schedule> scheduleEntities = new ArrayList<>();

            for (String idStr : scheduleIds) {
                Long id = Long.valueOf(idStr.trim()); // Chuyển đổi và loại bỏ khoảng trắng
                Schedule schedule = new Schedule();
                schedule.setId(id);
                scheduleEntities.add(schedule);
            }

            // Tạo đối tượng Movie
            movieDTO.setNewMovie(true); // Bạn có thể điều chỉnh điều này theo nhu cầu thực tế
            Movie movieEntity = modelMapper.map(movieDTO, Movie.class);
            Date currentDate = Date.valueOf(LocalDate.now());
            User userEntity = userRepository.findById(Long.valueOf(movieEntity.getUserAdd().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            movieEntity.setUserAdd(userEntity);
            movieEntity.setUserUpdate(userEntity);
            movieEntity.setImageUrl(Arrays.toString(file.getBytes()));
            movieEntity = movieRepository.save(movieEntity);

            // Xử lý các thể loại liên quan
            for (Category category : categoryEntities) {
                Category categoryItem = categoryRepository.findById(Long.valueOf(category.getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại"));
                CategoryMovie categoryMovieEntity = new CategoryMovie();
                categoryMovieEntity.setCategory(categoryItem);
                categoryMovieEntity.setMovie(movieEntity);
                categoryMovieRepository.save(categoryMovieEntity);
            }
            // Xử lý thêm lịch đăng liên quan
            for (Schedule schedule : scheduleEntities) {
                Schedule scheduleitem = scheduleRepository.findById(schedule.getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại"));
                ScheduleMovie scheduleMovieEntity = new ScheduleMovie();
                scheduleMovieEntity.setSchedule(scheduleitem);
                scheduleMovieEntity.setMovie(movieEntity);
                scheduleMovieRepository.save(scheduleMovieEntity);
            }
            // Chuyển đổi entity sang DTO
            return modelMapper.map(movieEntity, MovieDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo phim: " + e.getMessage(), e);
        }
    }

    @Override
    public MovieDTO update(MovieDTO movieDTO, MultipartFile file, String categorylist, String scheduleList) throws IOException {
        try {
            if (movieDTO == null) throw new RuntimeException("Bạn chưa nhập dữ liệu!");
            Movie movie = movieRepository.findById(movieDTO.getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy bộ phim"));
            User User = userRepository.findById(Long.valueOf(movieDTO.getUserUpdate().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            if (file == null) movieDTO.setImageUrl(movie.getImageUrl().getBytes());

            else movieDTO.setImageUrl(file.getBytes());
            if (categorylist == null) throw new RuntimeException("Vui là chọn thể loại của bộ phim!");
            List<String> categoryIds = Arrays.asList(categorylist.split(","));
            List<Category> list = new ArrayList<>();

            for (String idStr : categoryIds) {
//                Long id = Long.valueOf(idStr.trim()); // Chuyển đổi và loại bỏ khoảng trắng
                Category category = new Category();
                category.setId(idStr);
                list.add(category);
            }
            List<String> scheduleIds = Arrays.asList(scheduleList.split(","));
            List<Schedule> scheduleEntities = new ArrayList<>();

            for (String idStr : scheduleIds) {
                Long id = Long.valueOf(idStr.trim()); // Chuyển đổi và loại bỏ khoảng trắng
                Schedule schedule = new Schedule();
                schedule.setId(id);
                scheduleEntities.add(schedule);
            }
            movie.setId(movieDTO.getId());
            movie.setVnName(movieDTO.getVnName());
            movie.setCnName(movieDTO.getCnName());
            movie.setImageUrl(Arrays.toString(movieDTO.getImageUrl()));
            movie.setTimeAdd(movie.getTimeAdd());
            movie.setUserAdd(movie.getUserAdd());
            movie.setUserUpdate(User);
            movie.setNewMovie(movieDTO.getNewMovie());
            movie.setPrice(movieDTO.getPrice());
            movie.setAuthor(movieDTO.getAuthor());
            movie.setHotMovie(movieDTO.getHotMovie());
            movie.setVipMovie(movieDTO.getVipMovie());
            movie.setYear(movieDTO.getYear());
            movie.setEpisodeNumber(movieDTO.getEpisodeNumber());
            Movie movie_update = movieRepository.save(movie);
            List<CategoryMovie> CategoryMovie = categoryMovieRepository.findByMovie(movie);
            for (CategoryMovie CategoryMovie1 : CategoryMovie) {
                categoryMovieRepository.delete(CategoryMovie1);
            }
            for (Category category : list) {
                Category categoryitem = categoryRepository.findById(Long.valueOf(category.getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại"));
                CategoryMovie CategoryMovieupdate = new CategoryMovie();
                CategoryMovieupdate.setCategory(categoryitem);
                CategoryMovieupdate.setMovie(movie_update);
                categoryMovieRepository.save(CategoryMovieupdate);
            }
            List<ScheduleMovie> ScheduleMovie = scheduleMovieRepository.findByMovie(movie);
            for (ScheduleMovie item : ScheduleMovie) {
                scheduleMovieRepository.delete(item);
            }
            for (Schedule item : scheduleEntities) {
                Schedule schedule = scheduleRepository.findById(item.getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy lịch chiếu"));
                ScheduleMovie scheduleMovieEntity = new ScheduleMovie();
                scheduleMovieEntity.setSchedule(schedule);
                scheduleMovieEntity.setMovie(movie_update);
                scheduleMovieRepository.save(scheduleMovieEntity);
            }
            MovieDTO movieDTOMap = modelMapper.map(movie_update, MovieDTO.class);
            return movieDTOMap;

        } catch (Exception e) {
            throw new RuntimeException("Có lỗi khi chỉnh sửa bộ phim!");
        }

    }

    @Override
    public void delete(Long id) {
        try {
            Movie Movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bộ phim"));
            List<CategoryMovie> CategoryMovie = categoryMovieRepository.findByMovie(Movie);
            for (CategoryMovie CategoryMovie1 : CategoryMovie) {
                categoryMovieRepository.delete(CategoryMovie1);
            }
            movieRepository.delete(Movie);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi khi xóa bộ phim!");
        }
    }

    @Override
    public int totalItem() {
        return (int) movieRepository.count();
    }
}
