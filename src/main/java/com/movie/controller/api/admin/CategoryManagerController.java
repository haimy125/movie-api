package com.movie.controller.api.admin;

import com.movie.response.CategoryResponse;
import com.movie.dto.CategoryDTO;
import com.movie.service.admin.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/category")
public class CategoryManagerController {

    @Autowired
    private CategoryService categoryService;

    // Api hiển thị tất cả
    @GetMapping("/all")
    public CategoryResponse getAll(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        CategoryResponse result = new CategoryResponse();
        result.setPage(page); //  đưa ra tố trang hiển tại
        Pageable pageable = PageRequest.of(page - 1, limit); // khởi tạo phân trang
        result.setListResult(categoryService.getAll(pageable)); // gán dữ liệu
        result.setTotalPage((int) Math.ceil((double) (categoryService.totalItem()) / limit)); // đưa ra số phần tử theo trang 
        return result;
    }

    @GetMapping("/getbyname")
    public CategoryResponse getByName(@RequestParam("name") String name, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        CategoryResponse result = new CategoryResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(categoryService.getByName(name, pageable));
        result.setTotalPage((int) Math.ceil((double) (categoryService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getByid(@PathVariable Long id) {
        try {
            CategoryDTO categoryDTO = categoryService.getById(id);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        try {
            // Xử lý lỗi validation từ DTO
            if (result.hasErrors()) {
                Map<String, String> errors = result.getFieldErrors().stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage
                        ));
                return ResponseEntity.badRequest().body(errors);
            }

            CategoryDTO category = categoryService.save(categoryDTO);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        try {
            // Xử lý lỗi validation từ DTO
            if (result.hasErrors()) {
                Map<String, String> errors = result.getFieldErrors().stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage
                        ));
                return ResponseEntity.badRequest().body(errors);
            }

            categoryDTO.setId(id);
            CategoryDTO category = categoryService.update(categoryDTO);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            categoryService.deleteById(id);
            return new ResponseEntity<>("Xóa thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
