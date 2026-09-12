package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.Constant;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private ICategoryService cateService;

    private static final int PAGE_SIZE = 5;

    @GetMapping("/categories")
    public String listCategories(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            Model model) {

        keyword = keyword.trim();
        if (page < 1) page = 1;

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Category> categoryPage;

        if (!keyword.isEmpty()) {
            categoryPage = cateService.search(keyword, pageable);
        } else {
            categoryPage = cateService.findAll(pageable);
        }

        int totalPages = categoryPage.getTotalPages();
        if (totalPages < 1) totalPages = 1;
        if (page > totalPages) {
            page = totalPages;
            pageable = PageRequest.of(page - 1, PAGE_SIZE);
            categoryPage = !keyword.isEmpty() ? cateService.search(keyword, pageable) : cateService.findAll(pageable);
        }

        model.addAttribute("cateList", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", categoryPage.getTotalElements());
        model.addAttribute("pageSize", PAGE_SIZE);
        model.addAttribute("keyword", keyword);

        return "admin/list-category";
    }

    @GetMapping("/category/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/add-category";
    }

    @PostMapping("/category/insert")
    public String insertCategory(
            @RequestParam("name") String name,
            @RequestParam(value = "icon", required = false) MultipartFile iconFile,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (name == null || name.trim().length() < 2) {
            model.addAttribute("error", "Tên danh mục phải từ 2 ký tự trở lên!");
            model.addAttribute("name", name);
            return "admin/add-category";
        }

        Category category = new Category();
        category.setName(name.trim());

        if (iconFile != null && !iconFile.isEmpty()) {
            try {
                File dir = new File(Constant.DIR);
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = iconFile.getOriginalFilename();
                String ext = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf("."))
                        : ".png";
                String fname = System.currentTimeMillis() + ext;
                iconFile.transferTo(new File(Constant.DIR + "/" + fname));
                category.setIcon(fname);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            cateService.insert(category);
            return "redirect:/admin/categories?message=add_success";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể thêm danh mục: " + e.getMessage());
            model.addAttribute("name", name);
            return "admin/add-category";
        }
    }

    @GetMapping("/category/edit")
    public String editCategoryForm(@RequestParam("id") int id, Model model) {
        Category category = cateService.findById(id);
        if (category == null) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category);
        return "admin/edit-category";
    }

    @PostMapping("/category/update")
    public String updateCategory(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam(value = "icon", required = false) MultipartFile iconFile,
            Model model,
            RedirectAttributes redirectAttributes) {

        Category category = cateService.findById(id);
        if (category == null) {
            return "redirect:/admin/categories";
        }

        if (name == null || name.trim().length() < 2) {
            model.addAttribute("error", "Tên danh mục phải từ 2 ký tự trở lên!");
            model.addAttribute("category", category);
            return "admin/edit-category";
        }

        category.setName(name.trim());

        if (iconFile != null && !iconFile.isEmpty()) {
            try {
                File dir = new File(Constant.DIR);
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = iconFile.getOriginalFilename();
                String ext = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf("."))
                        : ".png";
                String fname = System.currentTimeMillis() + ext;
                iconFile.transferTo(new File(Constant.DIR + "/" + fname));
                category.setIcon(fname);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            cateService.update(category);
            return "redirect:/admin/categories?message=edit_success";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể cập nhật danh mục: " + e.getMessage());
            model.addAttribute("category", category);
            return "admin/edit-category";
        }
    }

    @GetMapping("/category/delete")
    public String deleteCategory(@RequestParam("id") int id) {
        try {
            cateService.delete(id);
            return "redirect:/admin/categories?message=delete_success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/categories?message=delete_error";
        }
    }
}
