package com.movie.controller.api.user;

import com.movie.response.CommentMovieResponse;
import com.movie.dto.CommentMovieDTO;
import com.movie.dto.MovieDTO;
import com.movie.dto.UserDTO;
import com.movie.service.user.CommentMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/comment/movie")
public class CommentMovieController {
    
    @Autowired
    private CommentMovieService commentMovieService;

    @GetMapping("/bymovie/{id}")
    public CommentMovieResponse getAll(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        CommentMovieResponse result = new CommentMovieResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(commentMovieService.getByMovie(id, pageable));
        result.setTotalPage((int) Math.ceil((double) (commentMovieService.totalItem()) / limit));
        return result;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestParam("movieid") Long movieid, @RequestParam("userid") Long userid, @RequestParam("content") String content) {
        try {
            UserDTO user = new UserDTO();
            user.setId(userid);
            MovieDTO movie = new MovieDTO();
            movie.setId(movieid);
            CommentMovieDTO commentMovieDto = new CommentMovieDTO();
            commentMovieDto.setMovie(movie);
            commentMovieDto.setUserAdd(user);
            commentMovieDto.setContent(content);
            commentMovieService.save(commentMovieDto);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
