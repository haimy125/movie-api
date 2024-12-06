package com.movie.service.user;

import com.movie.dto.UserFollowDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserFollowService {
    
    List<UserFollowDTO> findByUser(Long userId, Pageable pageable);

    void save(UserFollowDTO userFollowDTO);

    void delete(Long id);

    int totalItem();
}
