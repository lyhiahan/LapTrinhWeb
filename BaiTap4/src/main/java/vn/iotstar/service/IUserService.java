package vn.iotstar.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.User;

public interface IUserService {
    User login(String username, String password);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean register(String username, String password, String email, String fullname, String phone);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
    void insert(User user);
    boolean sendOtp(String email);
    boolean verifyOtp(String email, String otp);
    boolean resetPassword(String email, String newPassword);
    void updateProfile(User user);
    User findById(int id);
    void delete(int id) throws Exception;
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    List<User> findPaginated(int page, int pageSize);
    List<User> searchPaginated(String keyword, int page, int pageSize);
    Page<User> search(String keyword, Pageable pageable);
    int count();
    int countSearch(String keyword);
    void adminInsert(User user);
    void adminUpdate(User user);
}
