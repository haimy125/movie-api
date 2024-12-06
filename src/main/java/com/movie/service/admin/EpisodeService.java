package com.movie.service.admin;

import com.movie.dto.EpisodeDTO;
import io.jsonwebtoken.io.IOException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EpisodeService {

    List<EpisodeDTO> getAll(Pageable pageable);

    List<EpisodeDTO> getByMovie(Long movie_id, Pageable pageable);

    List<EpisodeDTO> getByMovie(Long movie_id);

    EpisodeDTO getById(Long id);

    EpisodeDTO getByEpId(Long id);

    EpisodeDTO create(EpisodeDTO EpisodeDTO, MultipartFile file, MultipartFile subtitles) throws IOException;

    EpisodeDTO update(EpisodeDTO EpisodeDTO, MultipartFile file, MultipartFile subtitles) throws IOException;

    void delete(Long id);

    int totalItem();
}
