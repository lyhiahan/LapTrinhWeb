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
@WebServlet(urlPatterns = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        IUserService service = new UserServiceImpl();
        if (!service.checkExistEmail(email)) {
            req.setAttribute("alert", "Email không tồn tại trong hệ thống!");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }
        boolean sent = service.sendOtp(email);
        if (sent) {
            resp.sendRedirect(req.getContextPath() + "/reset-password?email=" + email);
        } else {
            req.setAttribute("alert", "Không thể gửi email. Vui lòng thử lại sau!");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
        }
    }
}
