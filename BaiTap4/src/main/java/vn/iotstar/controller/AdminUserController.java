package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
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

import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.Constant;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRepository userRepository;

    private static final int PAGE_SIZE = 5;

    @GetMapping("/users")
    public String listUsers(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            Model model) {

        keyword = keyword.trim();
        if (page < 1) page = 1;

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<User> userPage;

        if (!keyword.isEmpty()) {
            userPage = userService.search(keyword, pageable);
        } else {
            userPage = userService.findAll(pageable);
        }

        int totalPages = userPage.getTotalPages();
        if (totalPages < 1) totalPages = 1;
        if (page > totalPages) {
            page = totalPages;
            pageable = PageRequest.of(page - 1, PAGE_SIZE);
            userPage = !keyword.isEmpty() ? userService.search(keyword, pageable) : userService.findAll(pageable);
        }

        model.addAttribute("userList", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalUsers", userPage.getTotalElements());
        model.addAttribute("pageSize", PAGE_SIZE);
        model.addAttribute("keyword", keyword);

        return "admin/list-user";
    }

    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        return "admin/add-user";
    }

    @PostMapping("/user/insert")
    public String insertUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "roleid", defaultValue = "5") int roleid,
            @RequestParam(value = "isActive", defaultValue = "false") boolean isActive,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile,
            Model model) {

        username = username != null ? username.trim() : "";
        email = email != null ? email.trim() : "";
        phone = phone != null ? phone.trim() : "";
        fullname = fullname != null ? fullname.trim() : "";

        // Validation
        if (username.length() < 3) {
            model.addAttribute("error", "Tên đăng nhập phải có ít nhất 3 ký tự!");
            populateForm(model, username, fullname, email, phone, roleid, isActive);
            return "admin/add-user";
        }
        if (password == null || password.trim().length() < 6) {
            model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            populateForm(model, username, fullname, email, phone, roleid, isActive);
            return "admin/add-user";
        }
        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Email không đúng định dạng hợp lệ!");
            populateForm(model, username, fullname, email, phone, roleid, isActive);
            return "admin/add-user";
        }
        if (userService.checkExistUsername(username)) {
            model.addAttribute("error", "Tên đăng nhập [" + username + "] đã tồn tại!");
            populateForm(model, username, fullname, email, phone, roleid, isActive);
            return "admin/add-user";
        }
        if (userService.checkExistEmail(email)) {
            model.addAttribute("error", "Email [" + email + "] đã được sử dụng!");
            populateForm(model, username, fullname, email, phone, roleid, isActive);
            return "admin/add-user";
        }
        if (!phone.isEmpty() && userService.checkExistPhone(phone)) {
            model.addAttribute("error", "Số điện thoại [" + phone + "] đã được sử dụng!");
            populateForm(model, username, fullname, email, phone, roleid, isActive);
            return "admin/add-user";
        }

        // Handle Avatar upload
        String avatarFileName = null;
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                File dir = new File(Constant.DIR);
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = avatarFile.getOriginalFilename();
                String ext = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf("."))
                        : ".png";
                avatarFileName = "user_" + System.currentTimeMillis() + ext;
                avatarFile.transferTo(new File(Constant.DIR + "/" + avatarFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        User user = new User();
        user.setUserName(username);
        user.setPassword(password.trim());
        user.setFullName(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRoleid(roleid);
        user.setIsActive(isActive);
        user.setAvatar(avatarFileName);
        user.setCreatedDate(new Date(System.currentTimeMillis()));

        try {
            userService.adminInsert(user);
            return "redirect:/admin/users?message=add_success";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi thêm người dùng: " + e.getMessage());
            populateForm(model, username, fullname, email, phone, roleid, isActive);
            return "admin/add-user";
        }
    }

    @GetMapping("/user/edit")
    public String editUserForm(@RequestParam("id") int id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/user/update")
    public String updateUser(
            @RequestParam("id") int id,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "roleid", defaultValue = "5") int roleid,
            @RequestParam(value = "isActive", defaultValue = "false") boolean isActive,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile,
            Model model) {

        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }

        email = email != null ? email.trim() : "";
        phone = phone != null ? phone.trim() : "";
        fullname = fullname != null ? fullname.trim() : "";

        // Check email uniqueness among other users
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent() && userByEmail.get().getId() != id) {
            model.addAttribute("error", "Email [" + email + "] đã được tài khoản khác sử dụng!");
            model.addAttribute("user", user);
            return "admin/edit-user";
        }

        if (!phone.isEmpty()) {
            Optional<User> userByPhone = userRepository.findByPhone(phone);
            if (userByPhone.isPresent() && userByPhone.get().getId() != id) {
                model.addAttribute("error", "Số điện thoại [" + phone + "] đã được tài khoản khác sử dụng!");
                model.addAttribute("user", user);
                return "admin/edit-user";
            }
        }

        // Handle avatar upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                File dir = new File(Constant.DIR);
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = avatarFile.getOriginalFilename();
                String ext = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf("."))
                        : ".png";
                String avatarFileName = "user_" + System.currentTimeMillis() + ext;
                avatarFile.transferTo(new File(Constant.DIR + "/" + avatarFileName));

                // Delete old avatar
                if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                    File oldFile = new File(Constant.DIR + "/" + user.getAvatar());
                    if (oldFile.exists()) oldFile.delete();
                }
                user.setAvatar(avatarFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        user.setFullName(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRoleid(roleid);
        user.setIsActive(isActive);

        if (password != null && !password.trim().isEmpty()) {
            if (password.trim().length() < 6) {
                model.addAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự!");
                model.addAttribute("user", user);
                return "admin/edit-user";
            }
            user.setPassword(password.trim());
        }

        try {
            userService.adminUpdate(user);
            return "redirect:/admin/users?message=edit_success";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi cập nhật người dùng: " + e.getMessage());
            model.addAttribute("user", user);
            return "admin/edit-user";
        }
    }

    @GetMapping("/user/delete")
    public String deleteUser(@RequestParam("id") int id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("account");
        if (loggedInUser != null && loggedInUser.getId() == id) {
            return "redirect:/admin/users?message=self_delete_forbidden";
        }

        try {
            userService.delete(id);
            return "redirect:/admin/users?message=delete_success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/users?message=delete_error";
        }
    }

    private void populateForm(Model model, String username, String fullname, String email,
                              String phone, int roleid, boolean isActive) {
        model.addAttribute("username", username);
        model.addAttribute("fullname", fullname);
        model.addAttribute("email", email);
        model.addAttribute("phone", phone);
        model.addAttribute("roleid", roleid);
        model.addAttribute("isActive", isActive);
    }
}
