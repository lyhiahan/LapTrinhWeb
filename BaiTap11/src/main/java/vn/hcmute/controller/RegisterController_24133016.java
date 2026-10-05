package vn.hcmute.controller;

import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.hcmute.service.IUserService_24133016;

@Controller
public class RegisterController_24133016 {

    private static final int MAX_USERNAME_LENGTH = 50;
    private static final int MAX_PASSWORD_LENGTH = 128;
    private static final int MAX_EMAIL_LENGTH = 150;
    private static final int MAX_FULLNAME_LENGTH = 50;
    private static final int MAX_PHONE_LENGTH = 15;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

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
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        username = username != null ? username.trim() : "";
        email = email != null ? email.trim() : "";
        fullname = fullname != null ? fullname.trim() : "";
        phone = phone != null ? phone.trim() : "";
        password = password != null ? password : "";
        confirmPassword = confirmPassword != null ? confirmPassword : "";

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

        if (username.length() > MAX_USERNAME_LENGTH || password.length() > MAX_PASSWORD_LENGTH
                || email.length() > MAX_EMAIL_LENGTH || fullname.length() > MAX_FULLNAME_LENGTH
                || phone.length() > MAX_PHONE_LENGTH) {
            model.addAttribute("alert", "Thông tin vượt quá độ dài cho phép!");
            return "register";
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            model.addAttribute("alert", "Email không đúng định dạng!");
            return "register";
        }

        vn.hcmute.entity.User_24133016 existUser = userService.findByUsername(username);
        if (existUser != null) {
            if (Boolean.TRUE.equals(existUser.getActive())) {
                model.addAttribute("alert", "Tên tài khoản đã tồn tại!");
                return "register";
            }
            boolean ownsPendingAccount = existUser.getEmail() != null
                    && existUser.getEmail().equalsIgnoreCase(email)
                    && userService.matchesPassword(existUser, password);
            if (!ownsPendingAccount) {
                model.addAttribute("alert", "Tên tài khoản đã tồn tại hoặc thông tin kích hoạt không đúng!");
                return "register";
            }
        }

        vn.hcmute.entity.User_24133016 existEmail = userService.findByEmail(email);
        if (existEmail != null && (existEmail.getUsername() == null
                || !existEmail.getUsername().equalsIgnoreCase(username))) {
            model.addAttribute("alert", "Email đã được sử dụng!");
            return "register";
        }

        boolean success;
        try {
            success = userService.register(username, password, email, fullname, phone);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("alert", "Tên tài khoản hoặc email đã được sử dụng!");
            return "register";
        }
        if (success) {
            VerifyOtpController_24133016.clearFailures(session);
            // RedirectAttributes encodes reserved characters in the query string.
            redirectAttributes.addAttribute("email", email);
            return "redirect:/verify-otp";
        } else {
            model.addAttribute("alert", "Không thể đăng ký hoặc gửi OTP. Nếu vừa yêu cầu mã, vui lòng chờ 60 giây rồi thử lại.");
            return "register";
        }
    }
}
