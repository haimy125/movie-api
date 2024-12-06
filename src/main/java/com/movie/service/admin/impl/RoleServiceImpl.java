package com.movie.service.admin.impl;

import com.movie.dto.RoleDTO;
import com.movie.entity.Role;
import com.movie.repository.admin.RoleRepository;
import com.movie.service.admin.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    // hiển thị tất cả quyền hạn
    @Override
    public List<RoleDTO> getAll(Pageable pageable) {

        // time kiếm tất cả quyền hạn
        List<Role> list = roleRepository.findAll(pageable).getContent();
        // kiểm tra nếu không có quyền hạn nào
        if (list.isEmpty()) {
            throw new RuntimeException("Không có quyền hạn nào!");
        }
        List<RoleDTO> roleDTOS = new ArrayList<>();
        for (Role role : list) {
            RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
            roleDTOS.add(roleDTO);
        }
        return roleDTOS;
    }

    // lấy quyền hạn theo tên
    @Override
    public List<RoleDTO> getByName(String name) {
        // tìm kiếm quyền hạn theo tên
        List<Role> list = roleRepository.findByName(name);
        // kiểm tra nếu không có quyền nào
        if (list.isEmpty()) {
            throw new RuntimeException("Không có quyền này!");
        }
        List<RoleDTO> RoleDTOS = new ArrayList<>();
        for (Role Role : list) {
            RoleDTO RoleDTO = modelMapper.map(Role, RoleDTO.class);
            RoleDTOS.add(RoleDTO);
        }
        return RoleDTOS;
    }

    // hiển thị quyền hạn theo id
    @Override
    public RoleDTO getById(Long id) {
        try {
            // tìm kiếm quyền hạn theo id
            Role Role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có quyền này!"));
            RoleDTO RoleDTO = modelMapper.map(Role, RoleDTO.class);
            return RoleDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // thêm mới quyền hạn
    @Override
    public RoleDTO create(RoleDTO role) {
        try {
            // kiểm tra dữ liệu nhập vào có trống hay không
            if (role == null) throw new RuntimeException("Không đòng dạng!");
            // kiểm tra tên quyền hạn có trống hay không
            if (role.getName() == null || role.getName().equals(""))
                throw new RuntimeException("Không hãy nhập tên quynen!");
            Role Role = modelMapper.map(role, Role.class);
            Role role_EP = roleRepository.save(Role);
            RoleDTO RoleDTO = modelMapper.map(role_EP, RoleDTO.class);
            return RoleDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định khi thêm quyền hạn!");
        }
    }

    // cập nhật quyền hạn
    @Override
    public RoleDTO update(RoleDTO role) {
        try {
            // kiểm tra dữ liệu nhập vào có trống hay không
            if (role == null) throw new RuntimeException("Dữ liệu nhập vào không thể để chống!");
            // tìm kiếm quyền hạn theo id
            Role Role = roleRepository.findById(role.getId()).orElseThrow(() -> new RuntimeException("Không có quyền này!"));
            modelMapper.map(role, Role);
            Role role_EP = roleRepository.save(Role);
            RoleDTO RoleDTO = modelMapper.map(role_EP, RoleDTO.class);
            return RoleDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định khi thêm quyền hình!");
        }
    }

    // xóa quyền hạn
    @Override
    public void delete(Long id) {
        try {
            // tìm kiếm quyền hạn theo id
            Role Role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có quyền này!"));
            roleRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định khi xóa quyền hạn!");
        }
    }
}
