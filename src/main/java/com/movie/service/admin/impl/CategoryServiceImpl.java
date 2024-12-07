package com.movie.service.admin.impl;

import com.movie.dto.CategoryDTO;
import com.movie.entity.Category;
import com.movie.repository.admin.CategoryRepository;
import com.movie.service.admin.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryDTO> getAll(Pageable pageable) {
        List<Category> categoryEntities = categoryRepository.findAll(pageable).getContent();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category categoryEntity : categoryEntities) {
            CategoryDTO categoryDTO = modelMapper.map(categoryEntity, CategoryDTO.class);
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }

    @Override
    public List<CategoryDTO> getByName(String name, Pageable pageable) {
        List<Category> categoryEntities = categoryRepository.findByNameLike("%" + name + "%", pageable);
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category categoryEntity : categoryEntities) {
            CategoryDTO categoryDTO = modelMapper.map(categoryEntity, CategoryDTO.class);
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }

    @Override
    public CategoryDTO getById(Long id) {
        try {
            Category categoryEntity = categoryRepository.findById(id).get();
            CategoryDTO categoryDTO = modelMapper.map(categoryEntity, CategoryDTO.class);
            return categoryDTO;
        } catch (Exception e) {
            throw new RuntimeException("Không có tài liệu nào ");
        }
    }

    @Override
    public CategoryDTO save(CategoryDTO category) {
        try {
            if (category == null)
                throw new RuntimeException("Không thêm được Category");
            if (category.getName() == null)
                throw new RuntimeException("Bạn chưa nhập tên loại phim!");
            Category categoryEntity = modelMapper.map(category, Category.class);
            Category categoryEntity1 = categoryRepository.save(categoryEntity);
            CategoryDTO categoryDTO = modelMapper.map(categoryEntity1, CategoryDTO.class);
            return categoryDTO;
        } catch (Exception e) {
            throw new RuntimeException("Không thêm được Category");
        }
    }

    @Override
    public CategoryDTO update(CategoryDTO category) {
        try {
            if (category == null)
                throw new RuntimeException(" Bạn chưa nhập dữ liệu!");
            if (category.getName() == null)
                throw new RuntimeException("Chọn tên loại phim!");
            Category categoryEntity = categoryRepository.findById(Long.valueOf(category.getId())).get();
            modelMapper.map(category, categoryEntity);
            Category categorySave = categoryRepository.save(categoryEntity);
            CategoryDTO categoryDTO = modelMapper.map(categorySave, CategoryDTO.class);
            return categoryDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi sảy ra khi cập nhật Loại phim!");
        }

    }

    @Override
    public void deleteById(Long id) {
        try {
            if (id == null)
                throw new RuntimeException("Chọn tên loại phim!");
            Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có loại phim nào có id = " + id));
            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi sảy ra khi xóa Loại phim!");
        }
    }

    @Override
    public int totalItem() {
        return (int) categoryRepository.count();
    }

}
