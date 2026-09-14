package vn.iotstar.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Category;

public interface ICategoryService {

    Category save(Category category);

    void update(Category category);

    void delete(int id) throws Exception;

    Category findById(int id);

    List<Category> findAll();

    Page<Category> findAll(Pageable pageable);

    List<Category> search(String keyword);

    Page<Category> search(String keyword, Pageable pageable);

    long count();

    long countSearch(String keyword);
}
