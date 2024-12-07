package com.movie.service.admin.impl;

import com.movie.dto.EpisodeDTO;
import com.movie.dto.MovieDTO;
import com.movie.dto.UserDTO;
import com.movie.entity.Episode;
import com.movie.entity.Movie;
import com.movie.entity.User;
import com.movie.repository.admin.EpisodeRepository;
import com.movie.repository.admin.MovieRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.service.admin.EpisodeService;
import io.jsonwebtoken.io.IOException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EpisodeServiceImpl implements EpisodeService {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<EpisodeDTO> getAll(Pageable pageable) {
        List<EpisodeDTO> result = new ArrayList<>();
        List<Episode> episode_entity = episodeRepository.findAll(pageable).getContent();
        if (episode_entity == null)
            throw new RuntimeException("Không có bộ phim nào");
        for (Episode episode : episode_entity) {
            EpisodeDTO dto = modelMapper.map(episode, EpisodeDTO.class);
            dto.setFileEpisodes(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<EpisodeDTO> getByMovie(Long movie_id, Pageable pageable) {
        List<EpisodeDTO> result = new ArrayList<>();
        Movie movie = movieRepository.findById(movie_id).orElseThrow(() -> new RuntimeException("Không tìm thấy phim nào có id là " + movie_id));
        List<Episode> episode_entity = episodeRepository.findByMovie(movie, pageable);
        if (episode_entity == null)
            throw new RuntimeException("Không có bộ phim nào");
        for (Episode episode : episode_entity) {
            EpisodeDTO dto = new EpisodeDTO();
            dto.setId(episode.getId());
            dto.setName(episode.getName());
            dto.setViews(episode.getViews());
            dto.setLikes(episode.getLikes());
            dto.setMovie(modelMapper.map(episode.getMovie(), MovieDTO.class));
            dto.setUserAdd(modelMapper.map(episode.getUserAdd(), UserDTO.class));
            dto.setUserUpdate(modelMapper.map(episode.getUserUpdate(), UserDTO.class));
            dto.setDescription(episode.getDescription());
            dto.setFileEpisodes(null);
            dto.setSubtitles(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<EpisodeDTO> getByMovie(Long movie_id) {
        List<EpisodeDTO> result = new ArrayList<>();
        Movie movie = movieRepository.findById(movie_id).orElseThrow(() -> new RuntimeException("Không tìm thấy phim nào có id là " + movie_id));
        List<Episode> episode_entity = episodeRepository.findByMovie(movie);
        Long totalviews = 0L;
        if (episode_entity == null)
            throw new RuntimeException("Không có bộ phim nào");
        for (Episode episode : episode_entity) {
            EpisodeDTO dto = new EpisodeDTO();
            dto.setId(episode.getId());
            dto.setName(episode.getName());
            dto.setViews(episode.getViews());
            dto.setLikes(episode.getLikes());
            dto.setMovie(modelMapper.map(episode.getMovie(), MovieDTO.class));
            dto.setUserAdd(modelMapper.map(episode.getUserAdd(), UserDTO.class));
            dto.setUserUpdate(modelMapper.map(episode.getUserUpdate(), UserDTO.class));
            dto.setFileEpisodes(null);
            dto.setSubtitles(null);
            totalviews = episode.getViews() + totalviews;
            result.add(dto);
        }
        if (totalviews != movie.getTotalViews()) {
            movie.setTotalViews(totalviews);
            movieRepository.save(movie);
        }
        return result;
    }

    @Override
    public EpisodeDTO getById(Long id) {
        try {
            Episode episode = episodeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có bộ phim nào có id là: " + id));
            MovieDTO movie = modelMapper.map(episode.getMovie(), MovieDTO.class);
            movie.setImageUrl(null);
            EpisodeDTO dto = new EpisodeDTO();
            dto.setId(episode.getId());
            dto.setName(episode.getName());
            dto.setViews(episode.getViews());
            dto.setLikes(episode.getLikes());
            dto.setMovie(movie);
            dto.setUserAdd(modelMapper.map(episode.getUserAdd(), UserDTO.class));
            dto.setUserUpdate(modelMapper.map(episode.getUserUpdate(), UserDTO.class));
            dto.setDescription(episode.getDescription());
            dto.setFileEpisodes(episode.getFileEpisodes());
            dto.setSubtitles(episode.getSubtitles());
            dto.setViews(episode.getViews());

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Không có bộ phịm nào");
        }
    }

    @Override
    public EpisodeDTO getByEpId(Long id) {
        try {
            Episode episode = episodeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có bộ phim nào có id là: " + id));
            MovieDTO movie = modelMapper.map(episode.getMovie(), MovieDTO.class);
            movie.setImageUrl(null);
            episode.setViews(episode.getViews() + 1);
            EpisodeDTO dto = new EpisodeDTO();
            dto.setId(episode.getId());
            dto.setName(episode.getName());
            dto.setViews(episode.getViews());
            dto.setLikes(episode.getLikes());
            dto.setMovie(movie);
            dto.setUserAdd(modelMapper.map(episode.getUserAdd(), UserDTO.class));
            dto.setUserUpdate(modelMapper.map(episode.getUserUpdate(), UserDTO.class));
            dto.setDescription(episode.getDescription());
            dto.setFileEpisodes(null);
            dto.setSubtitles(null);
            episodeRepository.save(episode);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Không có bộ phịm nào");
        }
    }

    @Override
    public EpisodeDTO create(EpisodeDTO episodeDTO, MultipartFile file, MultipartFile subtitles) throws IOException {
        try {
            if (episodeDTO == null)
                throw new RuntimeException("Bạn chưa nhập dữ liệu tập phim");
            if (episodeDTO.getName() == null || episodeDTO.getName().equals(""))
                throw new RuntimeException("Vui lòng nhập tên tập phim");
            if (file == null)
                throw new RuntimeException("Vui lòng chọn video của tập phim");
//            if (subtitles == null)
//                throw new RuntimeException("Vui lòng chọn file sub của tập phim");
            if (episodeDTO.getViews() == null)
                episodeDTO.setViews(0L);
            if (episodeDTO.getLikes() == null)
                episodeDTO.setLikes(0L);
            Movie movie = movieRepository.findById(episodeDTO.getMovie().getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy phim nào có id là " + episodeDTO.getMovie().getId()));
            User useradd = userRepository.findById(Long.valueOf(episodeDTO.getUserAdd().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng nào có id là " + episodeDTO.getUserAdd().getId()));
            User userupdate = userRepository.findById(Long.valueOf(episodeDTO.getUserUpdate().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng nào có id là " + episodeDTO.getUserUpdate().getId()));
            Episode episode = modelMapper.map(episodeDTO, Episode.class);
            episode.setMovie(movie);
            episode.setUserAdd(useradd);
            episode.setUserUpdate(userupdate);
            episode.setFileEpisodes(file.getBytes());
            if (subtitles != null)
                episode.setSubtitles(subtitles.getBytes());
            else
                episode.setSubtitles(null);
            episodeRepository.save(episode);
            EpisodeDTO result = modelMapper.map(episode, EpisodeDTO.class);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Không có bộ phim nào");
        }
    }

    @Override
    public EpisodeDTO update(EpisodeDTO episodeDTO, MultipartFile file, MultipartFile subtitles) throws IOException {
        try {
            if (episodeDTO == null)
                throw new RuntimeException("Bạn chưa nhập dữ liệu tập phim");
            if (episodeDTO.getName() == null || episodeDTO.getName().equals(""))
                throw new RuntimeException("Vui lòng nhập tên tập phim");

            if (episodeDTO.getViews() == null)
                episodeDTO.setViews(0L);
            if (episodeDTO.getLikes() == null)
                episodeDTO.setLikes(0L);
            Episode episode = episodeRepository.findById(episodeDTO.getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy tập phim nào có id là " + episodeDTO.getId()));

            User userupdate = userRepository.findById(Long.valueOf(episodeDTO.getUserUpdate().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng nào có id là " + episodeDTO.getUserUpdate().getId()));
            episode.setName(episodeDTO.getName());
            episode.setViews(episodeDTO.getViews());
            episode.setLikes(episodeDTO.getLikes());
            episode.setMovie(episode.getMovie());
            episode.setUserAdd(episode.getUserAdd());
            episode.setUserUpdate(userupdate);
            episode.setTimeAdd(episode.getTimeAdd());
            if (file == null)
                episode.setFileEpisodes(episode.getFileEpisodes());
            else
                episode.setFileEpisodes(file.getBytes());
            if (subtitles == null)
                episode.setSubtitles(episode.getSubtitles());
            else
                episode.setSubtitles(subtitles.getBytes());
            episodeRepository.save(episode);
            EpisodeDTO result = modelMapper.map(episode, EpisodeDTO.class);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Không có bộ phim nào");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Episode episode = episodeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy tập phim nào có id là " + id));
            episodeRepository.delete(episode);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi khi xóa tập phim");
        }
    }

    @Override
    public int totalItem() {
        return (int) episodeRepository.count();
    }
}
