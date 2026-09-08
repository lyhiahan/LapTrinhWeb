package vn.iotstar.service.impl;

import java.io.File;
import java.util.List;

import vn.iotstar.dao.IProductDao;
import vn.iotstar.dao.impl.ProductDaoImpl;
import vn.iotstar.entity.Product;
import vn.iotstar.service.IProductService;
import vn.iotstar.util.Constant;

public class ProductServiceImpl implements IProductService {

    IProductDao productDao = new ProductDaoImpl();

    @Override
    public void insert(Product product) {
        productDao.insert(product);
    }

    @Override
    public void update(Product newProduct) {
        Product oldProduct = productDao.findById(newProduct.getProductId());
        if (oldProduct != null) {
            oldProduct.setProductName(newProduct.getProductName());
            oldProduct.setPrice(newProduct.getPrice());
            oldProduct.setDescription(newProduct.getDescription());
            oldProduct.setCategory(newProduct.getCategory());
            if (newProduct.getImage() != null) {
                String oldImage = oldProduct.getImage();
                if (oldImage != null && !oldImage.startsWith("http")) {
                    File file = new File(Constant.DIR + "/" + oldImage);
                    if (file.exists()) file.delete();
                }
                oldProduct.setImage(newProduct.getImage());
            }
            productDao.update(oldProduct);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        Product product = productDao.findById(id);
        if (product != null && product.getImage() != null && !product.getImage().startsWith("http")) {
            File file = new File(Constant.DIR + "/" + product.getImage());
            if (file.exists()) file.delete();
        }
        productDao.delete(id);
    }

    @Override
    public Product findById(int id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> findNewest(int limit) {
        return productDao.findNewest(limit);
    }

    @Override
    public List<Product> findPaginated(int page, int pageSize) {
        return productDao.findPaginated(page, pageSize);
    }

    @Override
    public int count() {
        return productDao.count();
    }

    @Override
    public List<Product> findByCategoryId(int cateId) {
        return productDao.findByCategoryId(cateId);
    }

    @Override
    public int countByCategoryId(int cateId) {
        return productDao.countByCategoryId(cateId);
    }

    @Override
    public List<Product> findPaginatedByCategoryId(int cateId, int page, int pageSize) {
        return productDao.findPaginatedByCategoryId(cateId, page, pageSize);
    }
}
