package vn.iotstar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.security.CustomUserDetails;
import vn.iotstar.service.ProductService;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        model.addAttribute("products", productService.findAll(keyword, page, size));
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "products/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("mode", "create");
        return "products/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute ProductDTO dto,
                         BindingResult result,
                         @RequestParam(required = false) MultipartFile image,
                         Authentication authentication,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "products/form";
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        dto.setUserId(user.getId());
        try {
            productService.create(dto, image);
            redirect.addFlashAttribute("success", "Tạo sản phẩm thành công.");
            return "redirect:/products";
        } catch (Exception e) {
            result.reject("product.error", e.getMessage() != null ? e.getMessage() : "Lỗi khi lưu sản phẩm");
            model.addAttribute("mode", "create");
            return "products/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Authentication authentication, Model model, RedirectAttributes redirect) {
        ProductDTO dto = productService.findById(id);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN") || a.getAuthority().equalsIgnoreCase("admin"));
        if (!isAdmin && !dto.getUserId().equals(user.getId())) {
            redirect.addFlashAttribute("error", "Bạn không có quyền chỉnh sửa sản phẩm này.");
            return "redirect:/products";
        }
        model.addAttribute("productDTO", dto);
        model.addAttribute("mode", "edit");
        return "products/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute ProductDTO dto,
                       BindingResult result,
                       @RequestParam(required = false) MultipartFile image,
                       Authentication authentication,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "products/form";
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN") || a.getAuthority().equalsIgnoreCase("admin"));
        try {
            productService.update(id, dto, image, user.getId(), isAdmin);
            redirect.addFlashAttribute("success", "Cập nhật sản phẩm thành công.");
            return "redirect:/products";
        } catch (org.springframework.security.access.AccessDeniedException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/products";
        } catch (Exception e) {
            result.reject("product.error", e.getMessage() != null ? e.getMessage() : "Lỗi khi cập nhật sản phẩm");
            model.addAttribute("mode", "edit");
            return "products/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Authentication authentication, RedirectAttributes redirect) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN") || a.getAuthority().equalsIgnoreCase("admin"));
        try {
            productService.delete(id, user.getId(), isAdmin);
            redirect.addFlashAttribute("success", "Xóa sản phẩm thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Xóa sản phẩm thất bại: " + e.getMessage());
        }
        return "redirect:/products";
    }
}
