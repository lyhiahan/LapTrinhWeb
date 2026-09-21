package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vn.iotstar.model.Category;
import vn.iotstar.model.Product;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Khởi tạo các danh mục mẫu
        Category dienThoai = categoryRepository.save(new Category(null, "Điện thoại"));
        Category laptop = categoryRepository.save(new Category(null, "Laptop"));
        Category mayTinhBang = categoryRepository.save(new Category(null, "Máy tính bảng"));
        Category phuKien = categoryRepository.save(new Category(null, "Phụ kiện"));

        // Khởi tạo các sản phẩm với mức giá đan xen để kiểm tra chức năng sắp xếp giá tăng dần
        productRepository.save(new Product(null, "Chuột Logitech MX Master 3S", 2300000.0, "Chuột công thái học cao cấp", phuKien));
        productRepository.save(new Product(null, "Xiaomi Redmi Note 13", 4500000.0, "Màn hình AMOLED 120Hz, Pin 5000mAh", dienThoai));
        productRepository.save(new Product(null, "Tai nghe Sony WH-1000XM5", 7500000.0, "Tai nghe chống ồn đỉnh cao", phuKien));
        productRepository.save(new Product(null, "Samsung Galaxy A55", 8990000.0, "Khung kim loại, Camera 50MP OIS", dienThoai));
        productRepository.save(new Product(null, "iPad Gen 10", 9800000.0, "Chip A14 Bionic, Màn hình Liquid Retina 10.9 inch", mayTinhBang));
        productRepository.save(new Product(null, "Acer Aspire 5", 12500000.0, "Intel Core i5 Gen 13, 16GB RAM, SSD 512GB", laptop));
        productRepository.save(new Product(null, "Samsung Galaxy Tab S9", 19500000.0, "Màn hình Dynamic AMOLED 2X, Kèm bút S Pen", mayTinhBang));
        productRepository.save(new Product(null, "MacBook Air M2", 24990000.0, "Apple M2, Thiết kế siêu mỏng nhẹ, pin 18h", laptop));
        productRepository.save(new Product(null, "iPhone 15 Pro Max", 31990000.0, "Khung Titan, Chip A17 Pro, Camera Zoom 5x", dienThoai));
        productRepository.save(new Product(null, "Asus ROG Strix G16", 38900000.0, "Laptop Gaming Intel Core i9, RTX 4070", laptop));
    }
}
