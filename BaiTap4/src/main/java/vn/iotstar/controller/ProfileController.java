package vn.iotstar.controller;
import java.io.File;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
@SuppressWarnings("serial")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10
)
@WebServlet(urlPatterns = { "/profile", "/profile/update" })
public class ProfileController extends HttpServlet {
    private IUserService userService = new UserServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User currentUser = (User) session.getAttribute("account");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        try {
            String avatarPath = null;
            Part part = req.getPart("avatar");
            if (part != null && part.getSize() > 0) {
                String originalFileName = part.getSubmittedFileName();
                if (originalFileName != null && !originalFileName.isEmpty()) {
                    int index = originalFileName.lastIndexOf(".");
                    String ext = originalFileName.substring(index + 1);
                    String fileName = System.currentTimeMillis() + "." + ext;
                    File dir = new File(Constant.DIR + "/avatar");
                    if (!dir.exists()) dir.mkdirs();
                    File file = new File(dir, fileName);
                    part.write(file.getAbsolutePath());
                    avatarPath = "avatar/" + fileName;
                }
            }
            User updateUser = userService.findByUsername(currentUser.getUserName());
            if (updateUser == null) {
                updateUser = new User();
                updateUser.setUserName(currentUser.getUserName());
            }
            updateUser.setFullName(fullname);
            updateUser.setPhone(phone);
            if (avatarPath != null) {
                updateUser.setAvatar(avatarPath);
            }
            userService.updateProfile(updateUser);
            User refreshedUser = userService.findByUsername(currentUser.getUserName());
            session.setAttribute("account", refreshedUser);
            resp.sendRedirect(req.getContextPath() + "/profile?message=success");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi khi cập nhật hồ sơ: " + e.getMessage());
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
        }
    }
}
