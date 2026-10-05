package vn.hcmute.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.hcmute.service.IUserService_24133016;

@Controller
public class VerifyOtpController_24133016 {

    @Autowired
    private IUserService_24133016 userService;

    @GetMapping("/verify-otp")
    public String verifyOtpPage(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String doVerifyOtp(@RequestParam("email") String email,
                              @RequestParam(value = "otp", required = false) String otp,
                              @RequestParam(value = "action", required = false) String action,
                              Model model) {
        email = email != null ? email.trim() : "";

        // Gửi lại OTP
        if ("resend".equals(action)) {
            boolean sent = userService.sendOtp(email);
            if (sent) {
                model.addAttribute("success", "Đã gửi lại mã OTP mới! Vui lòng kiểm tra email.");
            } else {
                model.addAttribute("alert", "Không thể gửi lại mã OTP. Vui lòng thử lại sau.");
            }
            model.addAttribute("email", email);
            return "verify-otp";
        }

        otp = otp != null ? otp.trim() : "";
        if (otp.isEmpty() || !otp.matches("^[0-9]{6}$")) {
            model.addAttribute("alert", "Vui lòng nhập chính xác 6 chữ số của mã OTP!");
            model.addAttribute("email", email);
            return "verify-otp";
        }

        boolean isValid = userService.verifyOtp(email, otp);
        if (isValid) {
            return "redirect:/login?activated=true";
        } else {
            model.addAttribute("alert", "Mã OTP không đúng hoặc đã hết hạn!");
            model.addAttribute("email", email);
            return "verify-otp";
        }
    }
}
