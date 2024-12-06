package com.movie.controller.api.admin;

import com.movie.controller.output.User_OutPut;
import com.movie.dto.UserDTO;
import com.movie.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public User_OutPut getAll(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        User_OutPut result = new User_OutPut();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(userService.getAll(pageable));
        result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyname")
    public User_OutPut getByName(@RequestParam("name") String name, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        User_OutPut result = new User_OutPut();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(userService.getByName(name, pageable));
        result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyrole")
    public User_OutPut getbyrole(@RequestParam("roleid") Long roleid, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        User_OutPut result = new User_OutPut();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(userService.getByRole(roleid, pageable));
        result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/role")
    public ResponseEntity<?> update(@RequestParam("userid") Long userid, @RequestParam("roleid") Long roleid) {
        try {
            UserDTO user = userService.updateRole(userid, roleid);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/recharge")
    public ResponseEntity<?> recharge(@RequestParam("userid") Long userid, @RequestParam("point") Long point) {
        try {
            UserDTO user = userService.recharge(userid, point);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok("Xoa thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
