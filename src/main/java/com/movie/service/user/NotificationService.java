package com.movie.service.user;

import com.movie.dto.NotificationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getByUser(Long id, Pageable pageable);

    List<NotificationDTO> getByUser(Long id);

    int totalItems();
}
