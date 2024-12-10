package com.movie.response;

import com.movie.dto.CategoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryResponse {

    private int page;
    private int totalPage;
    private List<CategoryDTO> listResult = new ArrayList<>();
}
