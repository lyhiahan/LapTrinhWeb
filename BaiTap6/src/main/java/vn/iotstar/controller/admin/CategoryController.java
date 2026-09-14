package vn.iotstar.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.Constant;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping({ "", "/searchpaginated" })
    public String searchAndPaginate(
            @RequestParam(name = "name", required = false, defaultValue = "") String keyword,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            ModelMap model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        if (currentPage < 1) {
            currentPage = 1;
        }

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("id").descending());
        Page<Category> resultPage;

        String searchKeyword = keyword.trim();
        if (StringUtils.hasText(searchKeyword)) {
            resultPage = categoryService.search(searchKeyword, pageable);
            model.addAttribute("name", searchKeyword);
        } else {
            resultPage = categoryService.findAll(pageable);
            model.addAttribute("name", "");
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            if (totalPages > 5) {
                if (end <= 4) {
                    end = 5;
                } else if (currentPage + 2 >= totalPages) {
                    start = totalPages - 4;
                }
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryPage", resultPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", resultPage.getTotalElements());

        return "admin/categories/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        model.addAttribute("isEdit", false);
        return "admin/categories/addOrEdit";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Category category = categoryService.findById(id);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Danh mục không tồn tại!");
            return "redirect:/admin/categories";
        }

        model.addAttribute("category", category);
        model.addAttribute("isEdit", true);
        return "admin/categories/addOrEdit";
    }

    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(
            @ModelAttribute("category") Category category,
            @RequestParam(value = "iconFile", required = false) MultipartFile iconFile,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (category.getName() == null || category.getName().trim().length() < 2) {
            model.addAttribute("error", "Tên danh mục không được để trống và phải từ 2 ký tự trở lên!");
            model.addAttribute("category", category);
            model.addAttribute("isEdit", category.getId() > 0);
            return "admin/categories/addOrEdit";
        }

        category.setName(category.getName().trim());

        if (iconFile != null && !iconFile.isEmpty()) {
            try {
                File dir = new File(Constant.UPLOAD_DIRECTORY);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String originalFilename = iconFile.getOriginalFilename();
                String ext = ".png";
                if (originalFilename != null && originalFilename.contains(".")) {
                    ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String filename = System.currentTimeMillis() + ext;
                File targetFile = new File(dir, filename);
                iconFile.transferTo(targetFile);

                category.setIcon(filename);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Lỗi tải ảnh lên: " + e.getMessage());
                model.addAttribute("category", category);
                model.addAttribute("isEdit", category.getId() > 0);
                return "admin/categories/addOrEdit";
            }
        }

        try {
            if (category.getId() > 0) {
                categoryService.update(category);
                redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công!");
            } else {
                categoryService.save(category);
                redirectAttributes.addFlashAttribute("message", "Thêm mới danh mục thành công!");
            }
            return "redirect:/admin/categories";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("category", category);
            model.addAttribute("isEdit", category.getId() > 0);
            return "admin/categories/addOrEdit";
        }
    }

    @GetMapping({ "/{id}/products", "/products" })
    public String viewCategoryProducts(
            @PathVariable(name = "id", required = false) Integer pathId,
            @RequestParam(name = "id", required = false) Integer paramId,
            Model model,
            RedirectAttributes redirectAttributes) {

        int id = pathId != null ? pathId : (paramId != null ? paramId : 0);
        Category category = categoryService.findById(id);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Danh mục không tồn tại!");
            return "redirect:/admin/categories";
        }

        List<Product> products = productRepository.findByCategory_Id(id);
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        return "admin/categories/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa danh mục: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}
