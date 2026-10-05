package vn.hcmute.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.hcmute.service.IUserService_24133016;

@Controller
public class RegisterController_24133016 {

    @Autowired
    private IUserService_24133016 userService;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("confirmPassword") String confirmPassword,
                             @RequestParam("email") String email,
                             @RequestParam("fullname") String fullname,
                             @RequestParam("phone") String phone,
                             Model model) {
        username = username != null ? username.trim() : "";
        email = email != null ? email.trim() : "";
        fullname = fullname != null ? fullname.trim() : "";
        phone = phone != null ? phone.trim() : "";

        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("fullname", fullname);
        model.addAttribute("phone", phone);

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullname.isEmpty()) {
            model.addAttribute("alert", "Vui lòng nhập đầy đủ thông tin!");
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("alert", "Mật khẩu xác nhận không khớp!");
            return "register";
        }

        vn.hcmute.entity.User_24133016 existUser = userService.findByUsername(username);
        if (existUser != null && Boolean.TRUE.equals(existUser.getActive())) {
            model.addAttribute("alert", "Tên tài khoản đã tồn tại!");
            return "register";
        }

        vn.hcmute.entity.User_24133016 existEmail = userService.findByEmail(email);
        if (existEmail != null && Boolean.TRUE.equals(existEmail.getActive()) && !existEmail.getUsername().equals(username)) {
            model.addAttribute("alert", "Email đã được sử dụng!");
            return "register";
        }

        boolean success = userService.register(username, password, email, fullname, phone);
        if (success) {
            return "redirect:/verify-otp?email=" + email;
        } else {
            model.addAttribute("alert", "Đăng ký thất bại! Vui lòng thử lại.");
            return "register";
        }
    }
}
