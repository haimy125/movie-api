package com.movie.response;

import com.movie.dto.UserFollowDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserFollowResponse {
    private int page;
    private int totalPage;
    private List<UserFollowDTO> listResult = new ArrayList<>();
}
