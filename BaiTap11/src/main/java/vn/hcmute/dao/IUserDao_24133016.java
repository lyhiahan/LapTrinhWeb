package vn.hcmute.dao;

import java.util.List;
import vn.hcmute.entity.User_24133016;

public interface IUserDao_24133016 {
    void insert(User_24133016 user);
    void update(User_24133016 user);
    void delete(String username) throws Exception;
    User_24133016 findById(String username);
    User_24133016 findByEmail(String email);
    List<User_24133016> findAll();
    User_24133016 login(String username, String password);
}
