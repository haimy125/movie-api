package com.movie.controller.api.user;

import com.movie.dto.MovieDTO;
import com.movie.dto.UserDTO;
import com.movie.dto.UserRatingDTO;
import com.movie.service.user.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingsService;

    @PostMapping("/review")
    public ResponseEntity<?> save(@RequestParam("userid") Long userid, @RequestParam("movieid") Long movieid, @RequestParam("rating") Long rating) {
        try {
            UserDTO user = new UserDTO();
            user.setId(String.valueOf(userid));
            MovieDTO movie = new MovieDTO();
            movie.setId(movieid);
            UserRatingDTO ratings = new UserRatingDTO();
            ratings.setUser(user);
            ratings.setMovie(movie);
            ratings.setRating(rating);
            ratingsService.save(ratings);
            return ResponseEntity.ok("Đánh giá thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getuserandmovie")
    public ResponseEntity<?> getByMovieAndUser(@RequestParam("userid") Long userid, @RequestParam("movieid") Long movieid) {
        try {
            UserRatingDTO ratings = ratingsService.getByMovieAndUser(movieid, userid);
            return ResponseEntity.ok(ratings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
