package vn.hcmute.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vn.hcmute.service.IUserService_24133016;

@Controller
public class VerifyOtpController_24133016 {

    public static final String FAILURE_EMAIL_SESSION_KEY = "otpFailureEmail_24133016";
    public static final String FAILURE_COUNT_SESSION_KEY = "otpFailureCount_24133016";
    private static final int MAX_FAILURES_PER_SESSION = 5;

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
                              HttpSession session,
                              Model model) {
        email = email != null ? email.trim() : "";

        // Gửi lại OTP
        if ("resend".equals(action)) {
            boolean sent = userService.sendOtp(email);
            if (sent) {
                clearFailures(session);
                model.addAttribute("success", "Đã gửi lại mã OTP mới! Vui lòng kiểm tra email.");
            } else {
                model.addAttribute("alert", "Không thể gửi lại mã OTP. Vui lòng chờ ít nhất 60 giây rồi thử lại.");
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

        int failures = getFailures(session, email);
        if (failures >= MAX_FAILURES_PER_SESSION) {
            model.addAttribute("alert", "Bạn đã nhập sai quá nhiều lần. Vui lòng gửi lại mã OTP mới.");
            model.addAttribute("email", email);
            return "verify-otp";
        }

        boolean isValid = userService.verifyOtp(email, otp);
        if (isValid) {
            clearFailures(session);
            return "redirect:/login?activated=true";
        } else {
            failures = recordFailure(session, email);
            model.addAttribute("alert", failures >= MAX_FAILURES_PER_SESSION
                    ? "Bạn đã nhập sai quá nhiều lần. Vui lòng gửi lại mã OTP mới."
                    : "Mã OTP không đúng hoặc đã hết hạn!");
            model.addAttribute("email", email);
            return "verify-otp";
        }
    }

    private int getFailures(HttpSession session, String email) {
        Object storedEmail = session.getAttribute(FAILURE_EMAIL_SESSION_KEY);
        if (!(storedEmail instanceof String value) || !value.equalsIgnoreCase(email)) {
            clearFailures(session);
            return 0;
        }
        Object count = session.getAttribute(FAILURE_COUNT_SESSION_KEY);
        return count instanceof Integer value ? value : 0;
    }

    private int recordFailure(HttpSession session, String email) {
        int failures = getFailures(session, email) + 1;
        session.setAttribute(FAILURE_EMAIL_SESSION_KEY, email);
        session.setAttribute(FAILURE_COUNT_SESSION_KEY, failures);
        return failures;
    }

    public static void clearFailures(HttpSession session) {
        session.removeAttribute(FAILURE_EMAIL_SESSION_KEY);
        session.removeAttribute(FAILURE_COUNT_SESSION_KEY);
    }
}
