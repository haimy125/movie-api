package com.movie.service.admin.impl;

import com.movie.entity.Movie;
import com.movie.repository.admin.CategoryRepository;
import com.movie.repository.admin.MovieRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.service.admin.StaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaticServiceImpl implements StaticService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public int tongSoUser() {
        return (int) userRepository.count();
    }

    @Override
    public int tongSoCategory() {
        return (int) categoryRepository.count();
    }

    @Override
    public int tongSoMovie() {
        return (int) movieRepository.count();
    }

    @Override
    public int tongSoVipmovie() {
        List<Movie> list = movieRepository.findByVipMovie(true);
        return list.size();
    }

    @Override
    public int tongSoNoVipmovie() {
        List<Movie> list = movieRepository.findByVipMovie(true);
        List<Movie> listAll = movieRepository.findAll();
        listAll.removeAll(list);
        return listAll.size();
    }
}
