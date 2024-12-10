package com.movie.response;

import com.movie.dto.UserDTO  ;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserResponse {
    private int page;
    private int totalPage;
    private List<UserDTO  > listResult = new ArrayList<>();
}
