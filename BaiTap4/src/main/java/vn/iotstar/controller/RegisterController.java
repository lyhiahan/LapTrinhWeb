package vn.iotstar.controller;

import java.io.IOException;
import java.util.regex.Pattern;

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

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,30}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0[3|5|7|8|9])[0-9]{8}$");

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

        String username = req.getParameter("username") != null ? req.getParameter("username").trim() : "";
        String fullname = req.getParameter("fullname") != null ? req.getParameter("fullname").trim() : "";
        String email = req.getParameter("email") != null ? req.getParameter("email").trim() : "";
        String phone = req.getParameter("phone") != null ? req.getParameter("phone").trim() : "";
        String password = req.getParameter("password") != null ? req.getParameter("password") : "";
        String confirmPassword = req.getParameter("confirmPassword") != null ? req.getParameter("confirmPassword") : "";

        // Repopulate form values
        req.setAttribute("username", username);
        req.setAttribute("fullname", fullname);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);

        // 1. Server-side Validation
        if (username.isEmpty() || !USERNAME_PATTERN.matcher(username).matches()) {
            req.setAttribute("alert", "Tên tài khoản từ 3-30 ký tự, chỉ chứa chữ cái, số và dấu gạch dưới!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (fullname.isEmpty() || fullname.length() < 2 || fullname.length() > 50) {
            req.setAttribute("alert", "Họ và tên không hợp lệ (phải từ 2 đến 50 ký tự)!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            req.setAttribute("alert", "Định dạng địa chỉ email không hợp lệ!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (phone.isEmpty() || !PHONE_PATTERN.matcher(phone).matches()) {
            req.setAttribute("alert", "Số điện thoại không hợp lệ (phải gồm 10 chữ số của Việt Nam)!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            req.setAttribute("alert", "Mật khẩu bảo mật phải có ít nhất 6 ký tự!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("alert", "Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // 2. Kiểm tra trùng lặp CSDL
        IUserService service = new UserServiceImpl();

        if (service.checkExistUsername(username)) {
            req.setAttribute("alert", "Tên tài khoản này đã tồn tại, vui lòng chọn tên khác!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (service.checkExistEmail(email)) {
            req.setAttribute("alert", "Địa chỉ email này đã được sử dụng!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // 3. Thực hiện đăng ký
        boolean isSuccess = service.register(username, password, email, fullname, phone);
        if (isSuccess) {
            resp.sendRedirect(req.getContextPath() + "/verify-otp?email=" + email);
        } else {
            req.setAttribute("alert", "Có lỗi xảy ra trong quá trình tạo tài khoản. Vui lòng thử lại sau!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
        }
    }
}
