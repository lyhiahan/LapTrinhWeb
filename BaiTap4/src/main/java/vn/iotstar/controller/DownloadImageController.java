package vn.iotstar.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.util.Constant;
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/image")
public class DownloadImageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fileName = req.getParameter("fname");
        if (fileName == null || fileName.trim().isEmpty() || fileName.trim().equalsIgnoreCase("null")) {
            resp.sendRedirect("https://ui-avatars.com/api/?name=No+Image&background=f1f5f9&color=94a3b8");
            return;
        }
        File file = new File(Constant.DIR + "/" + fileName.trim());
        if (file.exists()) {
            String contentType = getServletContext().getMimeType(file.getName());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            resp.setContentType(contentType);
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.transferTo(resp.getOutputStream());
            }
        } else {
            resp.sendRedirect("https://ui-avatars.com/api/?name=No+Image&background=f1f5f9&color=94a3b8");
        }
    }
}
