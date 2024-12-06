package com.movie.repository.admin;

import com.movie.entity.Notification;
import com.movie.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user, Pageable pageable);

    List<Notification> findByUser(User user);
}
