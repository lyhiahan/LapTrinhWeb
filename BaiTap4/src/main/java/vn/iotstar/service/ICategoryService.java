package vn.iotstar.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.Category;

public interface ICategoryService {
    void insert(Category category);
    void update(Category category);
    void delete(int id) throws Exception;
    Category findById(int id);
    Category findByName(String name);
    List<Category> findAll();
    Page<Category> findAll(Pageable pageable);
    List<Category> search(String keyword);
    Page<Category> search(String keyword, Pageable pageable);
    int count();
    List<Category> findPaginated(int page, int pageSize);
    List<Category> searchPaginated(String keyword, int page, int pageSize);
    int countSearch(String keyword);
}
