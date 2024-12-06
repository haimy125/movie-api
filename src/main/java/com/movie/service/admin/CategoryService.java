package com.movie.service.admin;

import com.movie.dto.CategoryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    public List<CategoryDTO> getAll(Pageable pageable);

    public List<CategoryDTO> getByName(String name, Pageable pageable);

    public CategoryDTO getById(Long id);

    public CategoryDTO save(CategoryDTO category);

    public CategoryDTO update(CategoryDTO category);

    public void deleteById(Long id);

    int totalItem();
}
