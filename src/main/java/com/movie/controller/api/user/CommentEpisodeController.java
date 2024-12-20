package com.movie.controller.api.user;

import com.movie.response.CommentEpisodeResponse;
import com.movie.dto.CommentEpisodeDTO;
import com.movie.dto.EpisodeDTO;
import com.movie.dto.UserDTO;
import com.movie.service.user.CommentEpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/user/comment/episode")
public class CommentEpisodeController {
    
    @Autowired
    private CommentEpisodeService comment_Episode_Service;

    @GetMapping("/byep/{id}")
    public CommentEpisodeResponse getAll(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        CommentEpisodeResponse result = new CommentEpisodeResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(comment_Episode_Service.getByEpisode(id, pageable));
        result.setTotalPage((int) Math.ceil((double) (comment_Episode_Service.totalItem()) / limit));
        return result;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestParam("epid") Long epid, @RequestParam("userid") Long userid, @RequestParam("content") String content) {
        try {
            UserDTO user = new UserDTO();
            user.setId(userid);
            EpisodeDTO episodeDto = new EpisodeDTO();
            episodeDto.setId(epid);
            CommentEpisodeDTO commentMovieDto = new CommentEpisodeDTO();
            commentMovieDto.setEpisode(episodeDto);
            commentMovieDto.setUserAdd(user);
            commentMovieDto.setContent(content);
            commentMovieDto.setTimeAdd(Date.valueOf(LocalDate.now()));
            comment_Episode_Service.save(commentMovieDto);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
