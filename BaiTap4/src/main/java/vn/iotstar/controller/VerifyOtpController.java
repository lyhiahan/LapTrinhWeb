package vn.iotstar.controller;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/verify-otp")
public class VerifyOtpController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        req.setAttribute("email", email);
        req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String otp = req.getParameter("otp");
        String action = req.getParameter("action");
        IUserService service = new UserServiceImpl();
        if ("resend".equals(action)) {
            boolean sent = service.sendOtp(email);
            if (sent) {
                req.setAttribute("success", "Đã gửi lại mã OTP mới! Vui lòng kiểm tra email.");
            } else {
                req.setAttribute("alert", "Không thể gửi lại mã OTP. Vui lòng thử lại.");
            }
            req.setAttribute("email", email);
            req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
            return;
        }
        boolean isValid = service.verifyOtp(email, otp);
        if (isValid) {
            resp.sendRedirect(req.getContextPath() + "/login?activated=true");
        } else {
            req.setAttribute("alert", "Mã OTP không đúng hoặc đã hết hạn!");
            req.setAttribute("email", email);
            req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
        }
    }
}
