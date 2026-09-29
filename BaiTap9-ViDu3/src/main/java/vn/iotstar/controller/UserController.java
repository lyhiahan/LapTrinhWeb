package vn.iotstar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.UserDTO;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        model.addAttribute("users", userService.findAll(keyword, page, size));
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "users/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        UserDTO dto = new UserDTO();
        dto.setEnabled(true);
        dto.setRoleName("ROLE_USER");
        model.addAttribute("userDTO", dto);
        model.addAttribute("mode", "create");
        return "users/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute UserDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "users/form";
        }
        try {
            UserDTO created = userService.create(dto);
            redirect.addFlashAttribute("createdPassword", created.getInitialPassword());
            redirect.addFlashAttribute("createdUsername", created.getUsername());
            redirect.addFlashAttribute("success", "Tạo user [" + created.getUsername() + "] thành công! Mật khẩu ban đầu: " + created.getInitialPassword());
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            result.reject("user.error", e.getMessage());
            model.addAttribute("mode", "create");
            return "users/form";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            result.reject("user.error", "Username hoặc Email đã tồn tại trong hệ thống.");
            model.addAttribute("mode", "create");
            return "users/form";
        } catch (Exception e) {
            result.reject("user.error", "Tạo user thất bại: " + e.getMessage());
            model.addAttribute("mode", "create");
            return "users/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       org.springframework.security.core.Authentication authentication,
                       Model model) {
        model.addAttribute("userDTO", userService.findById(id));
        model.addAttribute("mode", "edit");
        boolean isSelf = false;
        if (authentication != null && authentication.getPrincipal() instanceof vn.iotstar.security.CustomUserDetails cud) {
            isSelf = cud.getId().equals(id);
        }
        model.addAttribute("isSelf", isSelf);
        return "users/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute UserDTO dto,
                       BindingResult result,
                       org.springframework.security.core.Authentication authentication,
                       Model model,
                       RedirectAttributes redirect) {
        Long currentUserId = null;
        boolean isSelf = false;
        if (authentication != null && authentication.getPrincipal() instanceof vn.iotstar.security.CustomUserDetails cud) {
            currentUserId = cud.getId();
            isSelf = cud.getId().equals(id);
        }
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("isSelf", isSelf);
            return "users/form";
        }
        try {
            userService.update(id, dto, currentUserId);
            redirect.addFlashAttribute("success", "Cập nhật user thành công.");
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            result.reject("user.error", e.getMessage());
            model.addAttribute("mode", "edit");
            model.addAttribute("isSelf", isSelf);
            return "users/form";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            result.reject("user.error", "Username hoặc Email đã tồn tại trong hệ thống.");
            model.addAttribute("mode", "edit");
            model.addAttribute("isSelf", isSelf);
            return "users/form";
        } catch (Exception e) {
            result.reject("user.error", "Cập nhật user thất bại: " + e.getMessage());
            model.addAttribute("mode", "edit");
            model.addAttribute("isSelf", isSelf);
            return "users/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         org.springframework.security.core.Authentication authentication,
                         RedirectAttributes redirect) {
        Long currentUserId = null;
        if (authentication != null && authentication.getPrincipal() instanceof vn.iotstar.security.CustomUserDetails cud) {
            currentUserId = cud.getId();
        }
        try {
            userService.delete(id, currentUserId);
            redirect.addFlashAttribute("success", "Xóa user thành công.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Xóa user thất bại: " + e.getMessage());
        }
        return "redirect:/users";
    }
}
