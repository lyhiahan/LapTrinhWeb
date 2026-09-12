package vn.iotstar.service.impl;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.Constant;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void insert(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void update(Category newCategory) {
        Optional<Category> opt = categoryRepository.findById(newCategory.getId());
        if (opt.isPresent()) {
            Category oldCategory = opt.get();
            oldCategory.setName(newCategory.getName());
            oldCategory.setPrice(newCategory.getPrice());
            if (newCategory.getIcon() != null && !newCategory.getIcon().isEmpty()) {
                String oldIcon = oldCategory.getIcon();
                if (oldIcon != null && !oldIcon.startsWith("http")) {
                    File file = new File(Constant.DIR + "/" + oldIcon);
                    if (file.exists()) file.delete();
                }
                oldCategory.setIcon(newCategory.getIcon());
            }
            categoryRepository.save(oldCategory);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        Optional<Category> opt = categoryRepository.findById(id);
        if (opt.isPresent()) {
            Category category = opt.get();
            if (category.getProducts() != null && !category.getProducts().isEmpty()) {
                throw new Exception("Không thể xóa danh mục đang có sản phẩm liên kết!");
            }
            if (category.getIcon() != null && !category.getIcon().startsWith("http")) {
                File file = new File(Constant.DIR + "/" + category.getIcon());
                if (file.exists()) file.delete();
            }
            categoryRepository.deleteById(id);
        }
    }

    @Override
    public Category findById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category findByName(String name) {
        Page<Category> page = categoryRepository.findByNameContainingIgnoreCase(name, PageRequest.of(0, 1));
        return page.hasContent() ? page.getContent().get(0) : null;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> search(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword, Pageable.unpaged()).getContent();
    }

    @Override
    public Page<Category> search(String keyword, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public int count() {
        return (int) categoryRepository.count();
    }

    @Override
    public List<Category> findPaginated(int page, int pageSize) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), pageSize);
        return categoryRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Category> searchPaginated(String keyword, int page, int pageSize) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), pageSize);
        return categoryRepository.findByNameContainingIgnoreCase(keyword, pageable).getContent();
    }

    @Override
    public int countSearch(String keyword) {
        return (int) categoryRepository.countByNameContainingIgnoreCase(keyword);
    }
}
