package com.movie.service.admin;

import com.movie.dto.RoleDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    List<RoleDTO> getAll(Pageable pageable);

    List<RoleDTO> getByName(String name);

    RoleDTO getById(Long id);

    RoleDTO create(RoleDTO role);

    RoleDTO update(RoleDTO role);

    void delete(Long id);
}
