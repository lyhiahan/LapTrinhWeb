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
@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        IUserService service = new UserServiceImpl();
        String alertMsg = "";
        if (service.checkExistEmail(email)) {
            alertMsg = "Email đã tồn tại!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }
        if (service.checkExistUsername(username)) {
            alertMsg = "Tài khoản đã tồn tại!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }
        boolean isSuccess = service.register(username, password, email, fullname, phone);
        if (isSuccess) {
            resp.sendRedirect(req.getContextPath() + "/verify-otp?email=" + email);
        } else {
            alertMsg = "Lỗi hệ thống!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
        }
    }
}
