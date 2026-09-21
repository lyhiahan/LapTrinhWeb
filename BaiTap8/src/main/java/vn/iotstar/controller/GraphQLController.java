package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.model.Category;
import vn.iotstar.model.Product;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;

import java.util.List;

@Controller
public class GraphQLController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // 1. Hiển thị tất cả product có price từ thấp đến cao
    @QueryMapping
    public List<Product> productsByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    // 2. Lấy tất cả product của 01 category
    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // Lấy tất cả sản phẩm
    @QueryMapping
    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    // Lấy sản phẩm theo ID
    @QueryMapping
    public Product productById(@Argument Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Lấy tất cả danh mục
    @QueryMapping
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }

    // Lấy danh mục theo ID
    @QueryMapping
    public Category categoryById(@Argument Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Schema mapping giải quyết trường products trong Category an toàn
    @SchemaMapping(typeName = "Category", field = "products")
    public List<Product> products(Category category) {
        return productRepository.findByCategoryId(category.getId());
    }

    // CRUD Category - Thêm mới
    @MutationMapping
    @Transactional
    public Category createCategory(@Argument String name) {
        Category cat = new Category();
        cat.setName(name);
        return categoryRepository.save(cat);
    }

    // CRUD Category - Cập nhật
    @MutationMapping
    @Transactional
    public Category updateCategory(@Argument Long id, @Argument String name) {
        return categoryRepository.findById(id).map(cat -> {
            cat.setName(name);
            return categoryRepository.save(cat);
        }).orElse(null);
    }

    // CRUD Category - Xóa
    @MutationMapping
    @Transactional
    public boolean deleteCategory(@Argument Long id) {
        return categoryRepository.findById(id).map(cat -> {
            categoryRepository.delete(cat);
            return true;
        }).orElse(false);
    }

    // CRUD Product - Thêm mới
    @MutationMapping
    @Transactional
    public Product createProduct(@Argument String title, @Argument Double price,
                                 @Argument String description, @Argument Long categoryId) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với ID: " + categoryId));
        Product prod = new Product();
        prod.setTitle(title);
        prod.setPrice(price);
        prod.setDescription(description);
        prod.setCategory(cat);
        return productRepository.save(prod);
    }

    // CRUD Product - Cập nhật
    @MutationMapping
    @Transactional
    public Product updateProduct(@Argument Long id, @Argument String title, @Argument Double price,
                                 @Argument String description, @Argument Long categoryId) {
        Product prod = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + id));
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với ID: " + categoryId));

        prod.setTitle(title);
        prod.setPrice(price);
        prod.setDescription(description);
        prod.setCategory(cat);
        return productRepository.save(prod);
    }

    // CRUD Product - Xóa
    @MutationMapping
    @Transactional
    public boolean deleteProduct(@Argument Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
