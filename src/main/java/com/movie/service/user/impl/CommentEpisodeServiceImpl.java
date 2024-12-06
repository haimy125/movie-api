package com.movie.service.user.impl;

import com.movie.dto.CommentEpisodeDTO;
import com.movie.dto.EpisodeDTO;
import com.movie.dto.UserDTO;
import com.movie.entity.CommentEpisode;
import com.movie.entity.Episode;
import com.movie.entity.User;
import com.movie.repository.admin.EpisodeRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.repository.user.CommentEpisodeRepository;
import com.movie.service.user.CommentEpisodeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentEpisodeServiceImpl implements CommentEpisodeService {
    
    @Autowired
    private CommentEpisodeRepository comment_EpisodeRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EpisodeRepository episodeRepository;

    @Override
    public List<CommentEpisodeDTO> getByEpisode(Long episodeid, Pageable pageable) {
        List<CommentEpisodeDTO> UserDTOS = new ArrayList<>();
        // tìm kiếm tất cả người dùng
        Episode episodeEnitty = episodeRepository.findById(episodeid).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        episodeEnitty.setFileEpisodes(null);
        episodeEnitty.setSubtitles(null);
        List<CommentEpisode> list = comment_EpisodeRepository.findByEpisode(episodeEnitty, pageable);
        // chuyển đổi sang DTO
        for (CommentEpisode item : list) {
            CommentEpisodeDTO commentMovieDto = new CommentEpisodeDTO();
            commentMovieDto.setId(item.getId());
            commentMovieDto.setContent(item.getContent());
            commentMovieDto.setEpisode(modelMapper.map(item.getEpisode(), EpisodeDTO.class)); // chuyển đổi sang DTO (modelmapper) item.getEpisode());
            commentMovieDto.setUserAdd(modelMapper.map(item.getUserAdd(), UserDTO.class)); // chuyển đổi sang DTO (modelmapper) item.getUser_add());
            commentMovieDto.setTimeAdd(item.getTimeAdd());
            UserDTOS.add(commentMovieDto);
        }
        return UserDTOS;
    }

    @Override
    public CommentEpisodeDTO save(CommentEpisodeDTO comment) {
        try {
            if (comment == null)
                throw new RuntimeException("Bạn chưa nhập bình luận");
            User user = userRepository.findById(Long.valueOf(comment.getUserAdd().getId())).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            Episode episodeEnitty = episodeRepository.findById(comment.getEpisode().getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy bộ phim"));
            CommentEpisode comment_Movie = modelMapper.map(comment, CommentEpisode.class);
            comment_Movie.setUserAdd(user);
            comment_Movie.setEpisode(episodeEnitty);
            comment_Movie.setTimeAdd(Date.valueOf(LocalDate.now()));
            comment_Movie.setContent(comment.getContent());
            comment_EpisodeRepository.save(comment_Movie);
            return comment;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu");
        }
    }

    @Override
    public int totalItem() {
        return (int) comment_EpisodeRepository.count();
    }
}
