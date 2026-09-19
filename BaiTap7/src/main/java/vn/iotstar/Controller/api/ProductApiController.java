package vn.iotstar.Controller.api;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.IStorageService;

@RestController
@RequestMapping(path = "/api/product")
public class ProductApiController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<Response>(new Response(true, "Thành công", productService.findAll()), HttpStatus.OK);
    }

    @PostMapping(path = "/getProduct")
    public ResponseEntity<?> getProduct(@Validated @RequestParam("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<Response>(new Response(true, "Thành công", product.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(new Response(false, "Thất bại", null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/addProduct")
    public ResponseEntity<?> addProduct(
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @Validated @RequestParam("discount") Double discount,
            @Validated @RequestParam("description") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @Validated @RequestParam(value = "status", defaultValue = "1") Short status) {

        Optional<Product> optProduct = productService.findByProductName(productName);
        if (optProduct.isPresent()) {
            return new ResponseEntity<Response>(new Response(false, "Sản phẩm này đã tồn tại trong hệ thống", optProduct.get()), HttpStatus.BAD_REQUEST);
        } else {
            Product product = new Product();
            product.setProductName(productName);
            product.setUnitPrice(unitPrice);
            product.setDiscount(discount);
            product.setDescription(description);
            product.setQuantity(quantity);
            product.setStatus(status);
            product.setCreateDate(new Date());

            Optional<Category> categoryOpt = categoryService.findById(categoryId);
            if (categoryOpt.isPresent()) {
                product.setCategory(categoryOpt.get());
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String fileName = storageService.getSorageFilename(imageFile, uuid.toString());
                storageService.store(imageFile, fileName);
                product.setImages(fileName);
            }

            productService.save(product);
            return new ResponseEntity<Response>(new Response(true, "Thêm sản phẩm thành công", product), HttpStatus.OK);
        }
    }

    @PutMapping(path = "/updateProduct")
    public ResponseEntity<?> updateProduct(
            @Validated @RequestParam("productId") Long productId,
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @Validated @RequestParam("discount") Double discount,
            @Validated @RequestParam("description") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @Validated @RequestParam(value = "status", defaultValue = "1") Short status) {

        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(new Response(false, "Không tìm thấy sản phẩm", null), HttpStatus.BAD_REQUEST);
        } else {
            Product product = optProduct.get();
            product.setProductName(productName);
            product.setUnitPrice(unitPrice);
            product.setDiscount(discount);
            product.setDescription(description);
            product.setQuantity(quantity);
            product.setStatus(status);

            Optional<Category> categoryOpt = categoryService.findById(categoryId);
            if (categoryOpt.isPresent()) {
                product.setCategory(categoryOpt.get());
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String fileName = storageService.getSorageFilename(imageFile, uuid.toString());
                storageService.store(imageFile, fileName);
                product.setImages(fileName);
            }

            productService.save(product);
            return new ResponseEntity<Response>(new Response(true, "Cập nhật sản phẩm thành công", product), HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/deleteProduct")
    public ResponseEntity<?> deleteProduct(@Validated @RequestParam("productId") Long productId) {
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(new Response(false, "Không tìm thấy sản phẩm", null), HttpStatus.BAD_REQUEST);
        } else {
            productService.delete(optProduct.get());
            return new ResponseEntity<Response>(new Response(true, "Xóa sản phẩm thành công", optProduct.get()), HttpStatus.OK);
        }
    }
}
