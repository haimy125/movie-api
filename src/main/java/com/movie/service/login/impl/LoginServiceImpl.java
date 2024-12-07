package com.movie.service.login.impl;

import com.movie.config.TokenUtil;
import com.movie.dto.UserDTO;
import com.movie.entity.Role;
import com.movie.entity.User;
import com.movie.repository.admin.RoleRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.service.login.LoginService;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    // chức năng đăng nhập
    @Override
    public UserDTO login(String username, String password) {
        try {
            //tìm kiếm tài khoản theo username
            User user = userRepository.findByUsername(username);
            // kiểm tra nếu không có tai khoản nào
            if (user == null)
                throw new RuntimeException("Tài khoản không tồn tại!");
            //kiểm tra nếu có tài khoản nhưng mật khẩu không chính xác
            Boolean isMatch = BCrypt.checkpw(password, user.getPassword());
            if (!isMatch)
                throw new RuntimeException("Tài khoản hoặc mật không chính xác!");
            // kiểm tra tài khoản có phải là admin hay không
            if (user.getRole().getName().equals("ROLE_ADMIN"))
                return modelMapper.map(user, UserDTO.class);
            // kiểm tra tài khoản có phải là user hay không
            if (user.getRole().getName().equals("ROLE_USER"))
                return modelMapper.map(user, UserDTO.class);
            // nếu không phải admin hoặc user thì lỗi
            throw new RuntimeException("Tài khoản không tồn tại!");
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định khi đăng nhập");

        }
    }

    // Đăng ký tài khoản
    @Override
    public UserDTO registerUser(UserDTO user) {
        try {
            User user_check = userRepository.findByUsername(user.getUsername());

            // kiểm tra nếu username đã tồn tại
            if (user_check != null)
                throw new RuntimeException("Tài khoản đã tồn tại!");

            // kiểm tra nếu có dữ liệu
            if (user == null)
                throw new RuntimeException("Không có dữ liệu!");

            // mã hóa mật khẩu
            String hashPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashPassword);

            if (user.getFullname() == null || user.getFullname().isEmpty())
                user.setFullname(user.getUsername());

            // kiểm tra quyền theo id
            Role role = roleRepository.findById(Long.valueOf(user.getRole().getId())).orElseThrow(() -> new RuntimeException("Không có quyền này!"));

            // Mapping DTO sang Entity
            User userEntity = modelMapper.map(user, User.class);
            userEntity.setRole(role);

            // lưu tài khoản
            User savedUser = userRepository.save(userEntity);

            // Mapping Entity sang DTO để trả về
            return modelMapper.map(savedUser, UserDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định khi đăng ký");
        }
    }

    @Override
    public UserDTO changePassword(Long id, String oldPassword, String newPassword, String confirmPassword) {
        try {
            if (oldPassword == null || oldPassword.equals(""))
                throw new RuntimeException("Bạn chưa nhập mật khẩu hiện tại!");
            if (newPassword == null || newPassword.equals(""))
                throw new RuntimeException("Bạn chưa nhập mật khẩu mới!");
            if (confirmPassword == null || confirmPassword.equals(""))
                throw new RuntimeException("Bạn chưa nhập lại mật khẩu!");
            if (newPassword.length() < 6)
                throw new RuntimeException("Mật khẩu có độ dài ít nhất 6 kí tự!");
            if (newPassword.length() > 25)
                throw new RuntimeException("Mật khẩu có độ dài tối đa 25 kí tự!");
            if (newPassword.contains(" "))
                throw new RuntimeException("Mật khẩu không thế có khoảng trắng!");
            if (!newPassword.equals(confirmPassword))
                throw new RuntimeException("Mật khẩu không trùng khớp!");
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng không!"));
            if (!BCrypt.checkpw(oldPassword, user.getPassword()))
                throw new RuntimeException("Mật khẩu hiện tại không chính xác!");
            String hashPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(hashPassword);
            User user_EP = userRepository.save(user);
            UserDTO UserDTO = modelMapper.map(user_EP, UserDTO.class);
            return UserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định khi đổi mật khẩu");
        }
    }

    // tìm tài khoản với username và email
    @Override
    public UserDTO checkUser(String username, String email) {
        try {
            //tìm kiếm tài khoản theo username và email
            User user = userRepository.findByUsernameAndEmail(username, email);
            // kiểm tra nếu không có tài khoản nào
            if (user == null)
                throw new RuntimeException("Tài khoản không tồn tại!");
            UserDTO UserDTO = modelMapper.map(user, UserDTO.class);
            return UserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định khi tìm kiếm tài khoản");
        }
    }

    // đặt lại mật khẩu
    @Override
    public UserDTO forgetPassword(Long id, String newPassword, String confirmPassword) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Tài khoản này không tồn tại!"));

            // kiểm tra nếu có dữ liệu
            if (user == null)
                throw new RuntimeException("Không có dữ liệu!");
            // kiểm tra nếu password không được nhập
            if (newPassword == null || newPassword.equals(""))
                throw new RuntimeException("Chưa nhập mật khẩu!");

            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("Mật không trùng khởp!");
            }
            // mã hóa mật khẩu
            String hashPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(hashPassword);
            User User = modelMapper.map(user, User.class);
            User user_EP = userRepository.save(User);
            UserDTO UserDTO = modelMapper.map(user_EP, UserDTO.class);
            return UserDTO;

        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định khi đăng ký");
        }

    }

    @Override
    public UserDTO checkUserByToken(String token) {
        if (token != null) {
            // kiểm tra xác định token
            boolean isTokenValid = TokenUtil.validateToken(token);

            if (!isTokenValid)
                throw new RuntimeException("Bạn đã hết phiên đăng nhập!");

            Long id = Long.valueOf(TokenUtil.getUserIdFromToken(token));
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));

            if (user != null) {
                UserDTO UserDTO = modelMapper.map(user, UserDTO.class);
                return UserDTO;
            }
        }
        throw new RuntimeException("Bạn đã hết phiên đăng nhập!");
    }

    @Override
    public UserDTO userProfile(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            UserDTO UserDTO = new UserDTO();
            UserDTO.setId(user.getId());
            UserDTO.setUsername(user.getUsername());
            UserDTO.setEmail(user.getEmail());
            UserDTO.setPoint(user.getPoint());
            UserDTO.setFullname(user.getFullname());
            UserDTO.setAvatar(null);
            UserDTO.setTimeAdd(user.getTimeAdd());
            UserDTO.setActive(user.getActive());
            UserDTO.setStatus(user.getStatus());
            return UserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định");
        }
    }

    @Override
    public UserDTO userByid(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            UserDTO UserDTO = new UserDTO();
            UserDTO.setId(user.getId());
            UserDTO.setUsername(user.getUsername());
            UserDTO.setFullname(user.getFullname());
            UserDTO.setEmail(user.getEmail());
            UserDTO.setPoint(user.getPoint());
            UserDTO.setAvatar(user.getAvatar());
            UserDTO.setTimeAdd(user.getTimeAdd());
            UserDTO.setActive(user.getActive());
            UserDTO.setStatus(user.getStatus());
            return UserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định");
        }
    }

    @Override
    public void uploadAvatar(Long id, MultipartFile file) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            if (user == null)
                throw new RuntimeException("Không có người dùng này!");
            if (file == null)
                user.setAvatar(user.getAvatar());
            else
                user.setAvatar(file.getBytes());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định");
        }

    }

    @Override
    public void updateInfo(Long id, String fullname, String email) {
        try {
            if (fullname == null || fullname.equals(""))
                throw new RuntimeException("Bạn chưa nhập họ tên!");
            if (email == null || email.equals(""))
                throw new RuntimeException("Bạn chưa nhập email!");
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            if (user == null)
                throw new RuntimeException("Không có người dùng này!");
            user.setFullname(fullname);
            user.setEmail(email);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định");
        }
    }
}
