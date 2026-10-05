package vn.hcmute.dao;

import java.util.List;
import vn.hcmute.entity.Category_24133016;

public interface ICategoryDao_24133016 {
    void insert(Category_24133016 category);
    void update(Category_24133016 category);
    void delete(int id) throws Exception;
    Category_24133016 findById(int id);
    List<Category_24133016> findAll();
    int count();
}
