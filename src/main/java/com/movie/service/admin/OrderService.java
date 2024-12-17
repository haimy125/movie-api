package com.movie.service.admin;

import com.movie.response.MonthlyRevenue;
import com.movie.dto.OrderDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAll(Pageable pageable);

    int totalItem();

    List<MonthlyRevenue> getMonthlyRevenue();

    void buyMovie(OrderDTO OrderDTO);

    OrderDTO getOrderById(Long id);

    OrderDTO getByUserIdAndMovieId(Long userId, Long movieId);

    List<OrderDTO> getOrderByUserId(Long userId, Pageable pageable);

    List<OrderDTO> getOrderByMovieId(Long movieId, Pageable pageable);

    String checkOrder(Long idMovie, String token);
}
