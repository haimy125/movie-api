package com.movie.controller.api.user;

import com.movie.controller.output.Movie_output;
import com.movie.dto.MovieDTO;
import com.movie.dto.OrderDTO;
import com.movie.dto.UserDTO;
import com.movie.service.admin.MovieService;
import com.movie.service.admin.OrderService;
import com.movie.service.user.MovieUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/movie")
public class MovieController {
    
    @Autowired
    private MovieUserService movie_UserService;
    
    @Autowired
    private MovieService movieService;
    
    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public Movie_output getAll(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movieService.getAll(pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyname")
    public Movie_output getbyname(@RequestParam("name") String name, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movieService.getByVn_name(name, pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbycategory")
    public Movie_output getbycategory(@RequestParam("categoryid") Long categoryid, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movie_UserService.getByCategoryid(categoryid, pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/list")
    public Movie_output getListMovie(@RequestParam(value = "category", required = false) Long category,
                                     @RequestParam(value = "year", required = false) Long year,
                                     @RequestParam(value = "sortBy", required = false) String sortBy,
                                     @RequestParam(value = "vip", required = false) Boolean vip,
                                     @RequestParam("page") int page,
                                     @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movie_UserService.getListMovie(category, year, vip, sortBy, pageable));
        result.setTotalPage((int) Math.ceil((double) (movie_UserService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/checkvip")
    public ResponseEntity<?> checkvip(@RequestParam("userid") Long userid, @RequestParam("movieid") Long movieid) {
        try {
            OrderDTO dto = orderService.getByUserIdAndMovieId(userid, movieid);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/buymovie")
    public ResponseEntity<?> buymovie(@RequestParam("userid") Long userid, @RequestParam("movieid") Long movieid) {
        try {
            UserDTO user = new UserDTO();
            user.setId(String.valueOf(userid));
            MovieDTO movie = new MovieDTO();
            movie.setId(String.valueOf(movieid));
            OrderDTO dto = new OrderDTO();
            dto.setUser(user);
            dto.setMovie(movie);
            orderService.buyMovie(dto);
            return new ResponseEntity<>("Mua phim thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
