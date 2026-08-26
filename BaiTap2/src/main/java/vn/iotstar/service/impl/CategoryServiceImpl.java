package vn.iotstar.service.impl;

import java.io.File;
import java.util.List;

import vn.iotstar.dao.ICategoryDao;
import vn.iotstar.dao.impl.CategoryDaoImpl;
import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.Constant;

public class CategoryServiceImpl implements ICategoryService {

    ICategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public void insert(Category category) {
        categoryDao.insert(category);
    }

    @Override
    public void update(Category newCategory) {
        Category oldCategory = categoryDao.findById(newCategory.getId());
        if (oldCategory != null) {
            oldCategory.setName(newCategory.getName());
            oldCategory.setPrice(newCategory.getPrice());
            if (newCategory.getIcon() != null) {
                String oldIcon = oldCategory.getIcon();
                if (oldIcon != null && !oldIcon.startsWith("http")) {
                    File file = new File(Constant.DIR + "/" + oldIcon);
                    if (file.exists()) file.delete();
                }
                oldCategory.setIcon(newCategory.getIcon());
            }
            categoryDao.update(oldCategory);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        categoryDao.delete(id);
    }

    @Override
    public Category findById(int id) {
        return categoryDao.findById(id);
    }

    @Override
    public Category findByName(String name) {
        return categoryDao.findByName(name);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> search(String keyword) {
        return categoryDao.search(keyword);
    }

    @Override
    public int count() {
        return categoryDao.count();
    }
}
