package vn.iotstar.dao;

import java.util.List;
import vn.iotstar.entity.User;

public interface IUserDao {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(int id);
    void insert(User user);
    void update(User user);
    void delete(int id) throws Exception;
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    List<User> findAll();
    List<User> findPaginated(int page, int pageSize);
    List<User> searchPaginated(String keyword, int page, int pageSize);
    int count();
    int countSearch(String keyword);
}

