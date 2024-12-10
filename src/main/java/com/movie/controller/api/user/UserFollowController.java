package com.movie.controller.api.user;

import com.movie.response.UserFollowResponse;
import com.movie.dto.MovieDTO;
import com.movie.dto.UserDTO;
import com.movie.dto.UserFollowDTO;
import com.movie.service.user.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/follow")
public class UserFollowController {
    
    @Autowired
    private UserFollowService userFollowService;

    @GetMapping("/getbyuser/{id}")
    public UserFollowResponse getAll(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        UserFollowResponse result = new UserFollowResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(userFollowService.findByUser(id, pageable));
        result.setTotalPage((int) Math.ceil((double) (userFollowService.totalItem()) / limit));
        return result;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFollow(@RequestParam("userid") Long userid, @RequestParam("movieid") Long movieid) {
        try {
            UserDTO user = new UserDTO();
            user.setId(userid);
            MovieDTO movie = new MovieDTO();
            movie.setId(movieid);
            UserFollowDTO dto = new UserFollowDTO();
            dto.setUser(user);
            dto.setMovie(movie);
            userFollowService.save(dto);
            return new ResponseEntity<>("Mua phim thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFollow(@PathVariable Long id) {
        try {
            userFollowService.delete(id);
            return new ResponseEntity<>("Xoa thanh cong", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
