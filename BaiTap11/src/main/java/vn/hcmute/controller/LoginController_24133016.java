package vn.hcmute.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vn.hcmute.entity.User_24133016;
import vn.hcmute.service.IUserService_24133016;

@Controller
public class LoginController_24133016 {

    @Autowired
    private IUserService_24133016 userService;

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model,
                            @RequestParam(value = "activated", required = false) String activated) {
        // Nếu đã đăng nhập thì chuyển hướng
        if (session.getAttribute("account") != null) {
            User_24133016 user = (User_24133016) session.getAttribute("account");
            if (user.getAdmin() != null && user.getAdmin()) {
                return "redirect:/admin/home";
            }
            return "redirect:/home";
        }
        if ("true".equals(activated)) {
            model.addAttribute("success", "Tài khoản đã được kích hoạt thành công! Vui lòng đăng nhập.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          HttpSession session, Model model) {
        username = username != null ? username.trim() : "";
        password = password != null ? password : "";

        model.addAttribute("username", username);

        if (username.isEmpty() || password.isEmpty()) {
            model.addAttribute("alert", "Tài khoản và mật khẩu không được để trống!");
            return "login";
        }

        // Kiểm tra tài khoản chưa kích hoạt
        User_24133016 checkUser = userService.findByUsername(username);
        if (checkUser != null && password.equals(checkUser.getPassword())
                && (checkUser.getActive() == null || !checkUser.getActive())) {
            model.addAttribute("alert", "Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác thực mã OTP.");
            model.addAttribute("inactiveEmail", checkUser.getEmail());
            return "login";
        }

        User_24133016 user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("account", user);
            // Admin -> trang quản trị, User -> trang chủ
            if (user.getAdmin() != null && user.getAdmin()) {
                return "redirect:/admin/home";
            }
            return "redirect:/home";
        } else {
            model.addAttribute("alert", "Tên tài khoản hoặc mật khẩu không chính xác!");
            return "login";
        }
    }
}
