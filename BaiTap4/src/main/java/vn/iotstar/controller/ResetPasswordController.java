package vn.iotstar.controller;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        req.setAttribute("email", email);
        req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email") != null ? req.getParameter("email").trim() : "";
        String otp = req.getParameter("otp") != null ? req.getParameter("otp").trim() : "";
        String newPassword = req.getParameter("newPassword") != null ? req.getParameter("newPassword") : "";
        String confirmPassword = req.getParameter("confirmPassword") != null ? req.getParameter("confirmPassword") : "";

        req.setAttribute("email", email);

        if (otp.isEmpty() || !otp.matches("^[0-9]{6}$")) {
            req.setAttribute("alert", "Mã OTP phải gồm đúng 6 chữ số!");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        if (newPassword.isEmpty() || newPassword.length() < 6) {
            req.setAttribute("alert", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("alert", "Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        IUserService service = new UserServiceImpl();
        User user = service.findByEmail(email);
        if (user == null || user.getOtp() == null || !otp.equals(user.getOtp())
                || user.getOtpExpiry() == null || user.getOtpExpiry().before(new java.util.Date())) {
            req.setAttribute("alert", "Mã OTP không đúng hoặc đã hết hạn!");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }
        boolean isReset = service.resetPassword(email, newPassword);
        if (isReset) {
            resp.sendRedirect(req.getContextPath() + "/login?resetSuccess=true");
        } else {
            req.setAttribute("alert", "Lỗi khi đặt lại mật khẩu!");
            req.setAttribute("email", email);
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
        }
    }
}
