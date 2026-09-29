package vn.iotstar.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.*;
import vn.iotstar.service.AuthService;
import vn.iotstar.service.OtpService;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final OtpService otpService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterDTO dto,
                           BindingResult result,
                           HttpServletRequest request,
                           RedirectAttributes redirect) {
        if (result.hasErrors()) return "auth/register";
        try {
            authService.register(dto, extractClientIp(request));
            redirect.addFlashAttribute("success", "OTP đã được gửi đến email.");
            redirect.addAttribute("email", dto.getEmail());
            return "redirect:/verify-otp";
        } catch (IllegalArgumentException | IllegalStateException e) {
            result.reject("register.error", e.getMessage());
            return "auth/register";
        } catch (Exception e) {
            result.reject("register.error", "Đăng ký thất bại: " + e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/verify-otp")
    public String verifyPage(@RequestParam(required = false) String email, Model model) {
        VerifyOtpDTO dto = new VerifyOtpDTO();
        dto.setEmail(email);
        model.addAttribute("verifyOtpDTO", dto);
        return "auth/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verify(@Valid @ModelAttribute VerifyOtpDTO dto,
                         BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "auth/verify-otp";
        try {
            if (!authService.verifyRegister(dto.getEmail(), dto.getOtp())) {
                result.reject("otp.error", "OTP không hợp lệ, hết hạn hoặc đã quá số lần thử.");
                return "auth/verify-otp";
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            result.reject("otp.error", e.getMessage());
            return "auth/verify-otp";
        }
        redirect.addFlashAttribute("success", "Xác nhận thành công. Hãy đăng nhập.");
        return "redirect:/login";
    }

    @PostMapping("/resend-register-otp")
    public String resend(@RequestParam String email,
                         HttpServletRequest request,
                         RedirectAttributes redirect) {
        try {
            otpService.sendRegisterOtp(email, extractClientIp(request));
            redirect.addFlashAttribute("success", "Đã gửi lại OTP.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể gửi lại OTP: " + e.getMessage());
        }
        redirect.addAttribute("email", email);
        return "redirect:/verify-otp";
    }

    @GetMapping("/forgot-password")
    public String forgot(Model model) {
        model.addAttribute("forgotPasswordDTO", new ForgotPasswordDTO());
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgot(@Valid @ModelAttribute ForgotPasswordDTO dto,
                         BindingResult result,
                         HttpServletRequest request,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "auth/forgot-password";
        try {
            authService.forgotPassword(dto.getEmail(), extractClientIp(request));
            redirect.addFlashAttribute("email", dto.getEmail());
            redirect.addFlashAttribute("success", "OTP đã được gửi.");
            return "redirect:/reset-password";
        } catch (IllegalArgumentException | IllegalStateException e) {
            result.reject("forgot.error", e.getMessage());
            return "auth/forgot-password";
        } catch (Exception e) {
            result.reject("forgot.error", "Gửi OTP thất bại: " + e.getMessage());
            return "auth/forgot-password";
        }
    }

    private String extractClientIp(HttpServletRequest request) {
        if (request == null) return "unknown";
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isBlank()) {
            return xf.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @GetMapping("/reset-password")
    public String reset(Model model) {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        Object email = model.asMap().get("email");
        if (email != null) dto.setEmail(email.toString());
        model.addAttribute("resetPasswordDTO", dto);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String reset(@Valid @ModelAttribute ResetPasswordDTO dto,
                        BindingResult result,
                        @RequestParam String otp,
                        RedirectAttributes redirect) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.reject("password.error", "Mật khẩu xác nhận không đúng.");
        }
        if (result.hasErrors()) return "auth/reset-password";
        try {
            authService.resetPasswordWithOtp(dto.getEmail(), otp, dto.getPassword());
            redirect.addFlashAttribute("success", "Đổi mật khẩu thành công.");
            return "redirect:/login";
        } catch (IllegalArgumentException | IllegalStateException e) {
            result.reject("otp.error", e.getMessage());
            return "auth/reset-password";
        } catch (Exception e) {
            result.reject("otp.error", "Đổi mật khẩu thất bại: " + e.getMessage());
            return "auth/reset-password";
        }
    }
}
