package com.movie.controller.api.admin;

import com.movie.response.EpisodeResponse;
import com.movie.dto.EpisodeDTO;
import com.movie.dto.MovieDTO;
import com.movie.dto.UserDTO;
import com.movie.service.admin.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/episode")
public class EpisodeManagerConltroler {

    @Autowired
    private EpisodeService episodeService;

    @GetMapping("/getBymovie/{id}")
    public EpisodeResponse getAll(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        EpisodeResponse result = new EpisodeResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(episodeService.getByMovie(id, pageable));
        result.setTotalPage((int) Math.ceil((double) (episodeService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getBymovie/all/{id}")
    public EpisodeResponse getbymovie(@PathVariable Long id) {
        EpisodeResponse result = new EpisodeResponse();
        result.setPage(1);
        result.setListResult(episodeService.getByMovie(id));
        result.setTotalPage((int) Math.ceil((double) (episodeService.totalItem()) / 10));
        return result;
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getByid(@PathVariable Long id) {
        try {
            EpisodeDTO episodeDTO = episodeService.getByEpId(id);
            return new ResponseEntity<>(episodeDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestParam("name") String name, @RequestParam("description") String description,
                                         @RequestParam("useradd") Long useradd, @RequestParam("views") Long views,
                                         @RequestParam("likes") Long likes, @RequestParam("movie") Long movie,
                                         @RequestParam(value = "videofile", required = false) MultipartFile videofile, @RequestParam(value = "subfile", required = false) MultipartFile subfile) {
        try {

            //Tạo user
            UserDTO user_dto = new UserDTO();
            user_dto.setId(useradd);
            MovieDTO movie_dto = new MovieDTO();
            movie_dto.setId(movie);
            //Tạo tâp phim
            EpisodeDTO episodeDTO = new EpisodeDTO();
            episodeDTO.setName(name);
            episodeDTO.setDescription(description);
            episodeDTO.setViews(views);
            episodeDTO.setLikes(likes);
            episodeDTO.setUserAdd(user_dto);
            episodeDTO.setUserUpdate(user_dto);
            episodeDTO.setMovie(movie_dto);
            episodeService.create(episodeDTO, videofile, subfile);
            return new ResponseEntity<>("Thêm mới thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestParam("name") String name, @RequestParam("description") String description,
                                         @RequestParam("useradd") Long useradd, @RequestParam("views") Long views,
                                         @RequestParam("likes") Long likes, @RequestParam("movie") Long movie,
                                         @RequestParam(value = "videofile", required = false) MultipartFile videofile, @RequestParam(value = "subfile", required = false) MultipartFile subfile) {
        try {

            //Tạo user
            UserDTO user_dto = new UserDTO();
            user_dto.setId(useradd);
            MovieDTO movie_dto = new MovieDTO();
            movie_dto.setId(movie);
            //Tạo tâp phim
            EpisodeDTO episodeDTO = new EpisodeDTO();
            episodeDTO.setId(id);
            episodeDTO.setName(name);
            episodeDTO.setDescription(description);
            episodeDTO.setViews(views);
            episodeDTO.setLikes(likes);
            episodeDTO.setUserAdd(user_dto);
            episodeDTO.setUserUpdate(user_dto);
            episodeDTO.setMovie(movie_dto);
            episodeService.update(episodeDTO, videofile, subfile);
            return new ResponseEntity<>("Thêm mới thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            episodeService.delete(id);
            return new ResponseEntity<>("Xóa thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/video/{id}")
    public ResponseEntity<byte[]> getVideo(@PathVariable Long id) {

        EpisodeDTO video = episodeService.getById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + video.getId() + ".mp4\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(video.getFileEpisodes());
    }

    @GetMapping("/sub/{id}")
    public ResponseEntity<byte[]> viewSUB(@PathVariable Long id) {
        EpisodeDTO fileDTO = episodeService.getById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDTO.getId() + ".srt\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileDTO.getSubtitles());

    }
}
