package com.movie.controller.api.admin;

import com.movie.controller.output.Category_OutPut;
import com.movie.dto.CategoryDTO;
import com.movie.service.admin.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/category")
public class CategoryManagerController {

    @Autowired
    private CategoryService categoryService;

    // Api hiển thị tất cả
    @GetMapping("/all")
    public Category_OutPut getAll(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Category_OutPut result = new Category_OutPut();
        result.setPage(page); //  đưa ra tố trang hiển tại
        Pageable pageable = PageRequest.of(page - 1, limit); // khởi tạo phân trang
        result.setListResult(categoryService.getAll(pageable)); // gán dữ liệu
        result.setTotalPage((int) Math.ceil((double) (categoryService.totalItem()) / limit)); // đưa ra số phần tử theo trang 
        return result;
    }

    @GetMapping("/getbyname")
    public Category_OutPut getByName(@RequestParam("name") String name, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        Category_OutPut result = new Category_OutPut();
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
    public ResponseEntity<?> create(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO category = categoryService.save(categoryDTO);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        try {
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
