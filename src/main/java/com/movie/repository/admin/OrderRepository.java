package com.movie.repository.admin;

import com.movie.entity.Movie;
import com.movie.entity.Order;
import com.movie.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByDateDesc(User userid, Pageable pageable);

    List<Order> findByUserAndMovie(User userid, Movie movieid);

    List<Order> findByMovie(Movie movieid, Pageable pageable);

    @Query("SELECT MONTH(o.date) AS month, YEAR(o.date) AS year, SUM(o.point) AS totalPoints " +
            "FROM Order o " +
            "WHERE o.status = 'COMPLETED' AND YEAR(o.date) = YEAR(CURRENT_DATE) " +
            "GROUP BY YEAR(o.date), MONTH(o.date) " +
            "ORDER BY YEAR(o.date) ASC, MONTH(o.date) ASC")
    List<Object[]> getMonthlyRevenue();

}
