package com.movie.service.admin.impl;

import com.movie.response.MonthlyRevenue;
import com.movie.dto.OrderDTO;
import com.movie.entity.Movie;
import com.movie.entity.Notification;
import com.movie.entity.Order;
import com.movie.entity.User;
import com.movie.repository.admin.MovieRepository;
import com.movie.repository.admin.NotificationRepository;
import com.movie.repository.admin.OrderRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.service.admin.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository order_Repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<OrderDTO> getAll(Pageable pageable) {
        List<OrderDTO> result = new ArrayList<>();
        List<Order> ordersEntities = order_Repository.findAll(pageable).getContent();
        if (ordersEntities == null) throw new RuntimeException("Không có bộ phim có tên này");
        for (Order orders : ordersEntities) {
            OrderDTO dto = modelMapper.map(orders, OrderDTO.class);
            result.add(dto);
        }
        return result;
    }

    @Override
    public int totalItem() {
        return (int) order_Repository.count();
    }

    @Override
    public List<MonthlyRevenue> getMonthlyRevenue() {
        List<Object[]> results = order_Repository.getMonthlyRevenue();
        List<MonthlyRevenue> revenueList = new ArrayList<>();

        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Integer year = (Integer) result[1];
            Long totalPoints = (Long) result[2];

            String monthYear = "Tháng " + month;
            MonthlyRevenue monthlyRevenue = new MonthlyRevenue();
            monthlyRevenue.setMonth(monthYear);
            monthlyRevenue.setTotalPoints(totalPoints);
            revenueList.add(monthlyRevenue);
        }

        return revenueList;
    }

    /**
     * @param orderDTO
     */
    @Override
    public void buyMovie(OrderDTO orderDTO) {

        if (orderDTO == null) throw new RuntimeException("Không có đơn hàng nào");
        Movie movie = movieRepository.findById(Long.valueOf(orderDTO.getMovie().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
        User user = userRepository.findById(Long.valueOf(orderDTO.getUser().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        if (user.getPoint() <= movie.getPrice().longValueExact())
            throw new RuntimeException("Xu còn lại của bạn không đủ vui lòng nạp thêm!");
        List<Order> orderscheck = order_Repository.findByUserAndMovie(user, movie);
        if (orderscheck.size() > 0) throw new RuntimeException("Bạn đã mua phim này rồi!");
        Order Order = modelMapper.map(orderDTO, Order.class);
        Date currentDate = Date.valueOf(LocalDate.now());
        Order.setMovie(movie);
        Order.setUser(user);
        Order.setPoint(movie.getPrice().longValueExact());
        Order.setStatus("COMPLETED");
        Order.setDate(currentDate);
        Order orders = order_Repository.save(Order);
        user.setPoint(user.getPoint() - movie.getPrice().longValueExact());
        userRepository.save(user);
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTimeAdd(Date.valueOf(LocalDate.now()));
        notification.setContent("Đã mua phim: " + movie.getVnName() + " hết " + orders.getPoint() + " xu \n số dư hiện tại là: " + user.getPoint() + " xu.");
        notification.setStatus(false);
        notificationRepository.save(notification);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        try {
            Order Order = order_Repository.findById(id).orElseThrow(() -> new Exception("Không tìm thấy đơn hàng"));
            return modelMapper.map(Order, OrderDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy đơn hang");
        }
    }

    @Override
    public OrderDTO getByUserIdAndMovieId(Long userId, Long movieId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
            Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new Exception("Không tìm thấy phim"));
            List<Order> Order = order_Repository.findByUserAndMovie(user, movie);
            if (Order.size() == 0) throw new Exception("Không tìm thấy đơn hang");
            Order orders = Order.get(0);
            return modelMapper.map(orders, OrderDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy đơn hang");
        }
    }

    @Override
    public List<OrderDTO> getOrderByUserId(Long userId, Pageable pageable) {

        List<OrderDTO> result = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        List<Order> ordersEntities = order_Repository.findByUserOrderByDateDesc(user, pageable);
        if (ordersEntities == null) throw new RuntimeException("Không có bộ phim có tên này");
        for (Order orders : ordersEntities) {
            OrderDTO dto = modelMapper.map(orders, OrderDTO.class);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<OrderDTO> getOrderByMovieId(Long movieId, Pageable pageable) {
        List<OrderDTO> result = new ArrayList<>();
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Không tìm thấy phim "));
        List<Order> ordersEntities = order_Repository.findByMovie(movie, pageable);
        if (ordersEntities == null) throw new RuntimeException("Không có bộ phim có tên này");
        for (Order orders : ordersEntities) {
            OrderDTO dto = modelMapper.map(orders, OrderDTO.class);
            result.add(dto);
        }
        return result;
    }

    /**
     * @param idMovie
     * @param token
     */
    @Override
    public String checkOrder(Long idMovie, String token) {
        Movie movie = movieRepository.findById(Long.valueOf(idMovie)).orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
        String userId = com.movie.config.TokenUtil.getUserIdFromToken(token);
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        List<Order> orderscheck = order_Repository.findByUserAndMovie(user, movie);
        if (orderscheck.size() > 0) throw new RuntimeException("Bạn đã mua phim này rồi!");

        Order newOrder = new Order();
        Date currentDate = Date.valueOf(LocalDate.now());
        newOrder.setUser(user);
        newOrder.setMovie(movie);
        newOrder.setPoint(movie.getPrice().longValueExact()); // Giả định điểm mặc định khi mua
        newOrder.setDate(currentDate); // Ngày mua hiện tại
        newOrder.setStatus("COMPLETED"); // Trạng thái hoàn thành

        // Lưu Order vào cơ sở dữ liệu
        order_Repository.save(newOrder);

        // Trả về thông báo thành công
        return "Bạn đã mua phim thành công!";
    }
}
