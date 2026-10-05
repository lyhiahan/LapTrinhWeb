package vn.hcmute.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hcmute.entity.Category_24133016;
import vn.hcmute.repository.CategoryRepository_24133016;
import vn.hcmute.service.ICategoryService_24133016;

@Service
@Transactional
public class CategoryServiceImpl_24133016 implements ICategoryService_24133016 {

    @Autowired
    private CategoryRepository_24133016 categoryRepository;

    @Override
    public List<Category_24133016> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category_24133016 findById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void insert(Category_24133016 category) {
        categoryRepository.save(category);
    }

    @Override
    public void update(Category_24133016 category) {
        categoryRepository.save(category);
    }

    @Override
    public void delete(int id) throws Exception {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new Exception("Không tìm thấy Category với ID: " + id);
        }
    }

    @Override
    public int count() {
        return (int) categoryRepository.count();
    }
}
