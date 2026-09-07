package vn.iotstar.dao;

import java.util.List;
import vn.iotstar.entity.Category;

public interface ICategoryDao {
    void insert(Category category);
    void update(Category category);
    void delete(int id) throws Exception;
    Category findById(int id);
    Category findByName(String name);
    List<Category> findAll();
    List<Category> search(String keyword);
    int count();
}
