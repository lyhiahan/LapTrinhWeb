package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.util.Constant;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/products")
    public String listProducts(Model model) {
        try {
            List<Product> productList = productService.findAll();
            model.addAttribute("productList", productList);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Không thể hiển thị danh sách do lỗi CSDL: " + e.getMessage());
        }
        return "admin/list-product";
    }

    @GetMapping("/product/add")
    public String addProductForm(Model model) {
        List<Category> cateList = categoryService.findAll();
        model.addAttribute("cateList", cateList);
        return "admin/add-product";
    }

    @PostMapping("/product/insert")
    public String insertProduct(
            @RequestParam("productName") String productName,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestParam("price") double price,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("image") MultipartFile imageFile,
            Model model) {

        Category category = categoryService.findById(categoryId);
        if (category == null) {
            model.addAttribute("error", "Danh mục đã chọn không tồn tại!");
            model.addAttribute("cateList", categoryService.findAll());
            return "admin/add-product";
        }

        if (imageFile == null || imageFile.isEmpty()) {
            model.addAttribute("error", "Vui lòng chọn hình ảnh đại diện cho sản phẩm!");
            model.addAttribute("cateList", categoryService.findAll());
            return "admin/add-product";
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setCreatedDate(new Date(System.currentTimeMillis()));

        try {
            File dir = new File(Constant.DIR + "/product");
            if (!dir.exists()) dir.mkdirs();

            String originalFilename = imageFile.getOriginalFilename();
            String ext = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".png";
            String fname = System.currentTimeMillis() + ext;
            imageFile.transferTo(new File(dir, fname));
            product.setImage("product/" + fname);

            productService.insert(product);
            return "redirect:/admin/products?message=add_success";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi khi lưu sản phẩm: " + e.getMessage());
            model.addAttribute("cateList", categoryService.findAll());
            return "admin/add-product";
        }
    }

    @GetMapping("/product/edit")
    public String editProductForm(@RequestParam("id") int id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("cateList", categoryService.findAll());
        return "admin/edit-product";
    }

    @PostMapping("/product/update")
    public String updateProduct(
            @RequestParam("productId") int productId,
            @RequestParam("productName") String productName,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestParam("price") double price,
            @RequestParam("categoryId") int categoryId,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            Model model) {

        Product product = productService.findById(productId);
        if (product == null) {
            return "redirect:/admin/products";
        }

        Category category = categoryService.findById(categoryId);
        if (category == null) {
            model.addAttribute("error", "Danh mục đã chọn không hợp lệ!");
            model.addAttribute("product", product);
            model.addAttribute("cateList", categoryService.findAll());
            return "admin/edit-product";
        }

        product.setProductName(productName);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                File dir = new File(Constant.DIR + "/product");
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = imageFile.getOriginalFilename();
                String ext = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf("."))
                        : ".png";
                String fname = System.currentTimeMillis() + ext;
                imageFile.transferTo(new File(dir, fname));

                // Delete old image
                if (product.getImage() != null && !product.getImage().isEmpty()) {
                    File oldFile = new File(Constant.DIR + "/" + product.getImage());
                    if (oldFile.exists()) oldFile.delete();
                }
                product.setImage("product/" + fname);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            productService.update(product);
            return "redirect:/admin/products?message=edit_success";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            model.addAttribute("product", product);
            model.addAttribute("cateList", categoryService.findAll());
            return "admin/edit-product";
        }
    }

    @GetMapping("/product/delete")
    public String deleteProduct(@RequestParam("id") int id) {
        try {
            productService.delete(id);
            return "redirect:/admin/products?message=delete_success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/products?message=delete_error";
        }
    }
}
