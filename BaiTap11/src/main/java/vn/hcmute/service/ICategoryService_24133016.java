package vn.hcmute.service;

import java.util.List;
import vn.hcmute.entity.Category_24133016;

public interface ICategoryService_24133016 {
    List<Category_24133016> findAll();
    Category_24133016 findById(int id);
    void insert(Category_24133016 category);
    void update(Category_24133016 category);
    void delete(int id) throws Exception;
    int count();
}
