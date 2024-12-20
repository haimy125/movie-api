package com.movie.service.user.impl;

import com.movie.dto.UserFollowDTO;
import com.movie.entity.Movie;
import com.movie.entity.User;
import com.movie.entity.UserFollow;
import com.movie.repository.admin.MovieRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.repository.user.UserFollowRepository;
import com.movie.service.user.UserFollowService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserFollowServiceImpl implements UserFollowService {
    
    @Autowired
    private UserFollowRepository userFollowRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<UserFollowDTO> findByUser(Long userId, Pageable pageable) {
        List<UserFollowDTO> UserDTOS = new ArrayList<>();
        // tìm kiếm tất cả người dùng
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        List<UserFollow> list = userFollowRepository.findByUser(user, pageable);
        // chuyển đổi sang DTO
        for (UserFollow item : list) {
            UserFollowDTO commentMovieDto = modelMapper.map(item, UserFollowDTO.class);
            UserDTOS.add(commentMovieDto);
        }
        return UserDTOS;
    }

    @Override
    public void save(UserFollowDTO userFollowDTO) {
        if (userFollowDTO == null)
            throw new RuntimeException("Không tìm thấy người dùng");
        User user = userRepository.findById(Long.valueOf(userFollowDTO.getUser().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        Movie movie = movieRepository.findById(Long.valueOf(userFollowDTO.getMovie().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy ngollectors"));
        List<UserFollow> check = userFollowRepository.findByUserAndMovie(user, movie);
        if (check.size() > 0)
            throw new RuntimeException("Bạn đã theo dõi phim này");
        UserFollow userFollowEntity = modelMapper.map(userFollowDTO, UserFollow.class);
        userFollowEntity.setUser(user);
        userFollowEntity.setMovie(movie);
        userFollowRepository.save(userFollowEntity);
    }

    @Override
    public void delete(Long id) {
        try {
            UserFollow userFollowEntity = userFollowRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            userFollowRepository.delete(userFollowEntity);
        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }
    }

    @Override
    public int totalItem() {
        return (int) userFollowRepository.count();
    }

    /**
     * @param userId
     * @param movieId
     */
    @Override
    public void deleteByUserIdAndMovieId(Long userId, Long movieId) {
        // Tìm User, nếu không tìm thấy, ném ngoại lệ
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Tìm Movie, nếu không tìm thấy, ném ngoại lệ
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));

        // Lấy danh sách UserFollow theo User và Movie
        List<UserFollow> userFollows = userFollowRepository.findByUserAndMovie(user, movie);

        // Nếu không tồn tại UserFollow nào, ném ngoại lệ
        if (userFollows.isEmpty()) {
            throw new RuntimeException("Không tìm thấy dữ liệu liên kết giữa người dùng và phim");
        }

        // Xóa UserFollow đầu tiên trong danh sách
        userFollowRepository.delete(userFollows.get(0));
    }
}
