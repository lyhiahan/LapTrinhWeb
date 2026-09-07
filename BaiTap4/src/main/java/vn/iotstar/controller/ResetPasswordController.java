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

        String email = req.getParameter("email");
        String otp = req.getParameter("otp");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        IUserService service = new UserServiceImpl();

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("alert", "Mật khẩu xác nhận không khớp!");
            req.setAttribute("email", email);
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        User user = service.findByEmail(email);
        if (user == null || user.getOtp() == null || !otp.equals(user.getOtp())
                || user.getOtpExpiry() == null || user.getOtpExpiry().before(new java.util.Date())) {
            req.setAttribute("alert", "Mã OTP không đúng hoặc đã hết hạn!");
            req.setAttribute("email", email);
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
