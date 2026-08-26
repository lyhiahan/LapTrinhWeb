package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(urlPatterns = { "/admin/categories", "/admin/category/add", "/admin/category/insert",
        "/admin/category/edit", "/admin/category/update", "/admin/category/delete" })
public class CategoryController extends HttpServlet {

    ICategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.contains("/admin/categories")) {
            // Danh sách
            try {
                List<Category> cateList = cateService.findAll();
                req.setAttribute("cateList", cateList);
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Không thể hiển thị danh sách do lỗi CSDL: " + e.getMessage());
            }
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/list-category.jsp");
            dispatcher.forward(req, resp);

        } else if (url.contains("/admin/category/add")) {
            // Form thêm mới
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/add-category.jsp");
            dispatcher.forward(req, resp);

        } else if (url.contains("/admin/category/edit")) {
            // Form sửa
            String id = req.getParameter("id");
            Category category = cateService.findById(Integer.parseInt(id));
            req.setAttribute("category", category);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/edit-category.jsp");
            dispatcher.forward(req, resp);

        } else if (url.contains("/admin/category/delete")) {
            // Xóa
            String id = req.getParameter("id");
            try {
                cateService.delete(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("/admin/category/insert")) {
            // Thêm mới
            Category category = new Category();
            try {
                String name = req.getParameter("name");
                category.setName(name);

                String priceStr = req.getParameter("price");
                if (priceStr != null && !priceStr.isEmpty()) {
                    category.setPrice(Double.parseDouble(priceStr));
                }

                Part part = req.getPart("icon");
                if (part != null && part.getSize() > 0) {
                    String originalFileName = part.getSubmittedFileName();
                    if (originalFileName != null && !originalFileName.isEmpty()) {
                        int index = originalFileName.lastIndexOf(".");
                        String ext = originalFileName.substring(index + 1);
                        String fileName = System.currentTimeMillis() + "." + ext;
                        File dir = new File(Constant.DIR + "/category");
                        if (!dir.exists()) dir.mkdirs();
                        File file = new File(dir, fileName);
                        part.write(file.getAbsolutePath());
                        category.setIcon("category/" + fileName);
                    }
                }
                cateService.insert(category);
                resp.sendRedirect(req.getContextPath() + "/admin/categories?message=add_success");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi khi thêm danh mục: " + e.getMessage());
                req.getRequestDispatcher("/views/admin/add-category.jsp").forward(req, resp);
            }

        } else if (url.contains("/admin/category/update")) {
            // Cập nhật
            Category category = new Category();
            try {
                String idStr = req.getParameter("id");
                String name = req.getParameter("name");
                if (idStr != null) {
                    category.setId(Integer.parseInt(idStr));
                }
                category.setName(name);

                String priceStr = req.getParameter("price");
                if (priceStr != null && !priceStr.isEmpty()) {
                    category.setPrice(Double.parseDouble(priceStr));
                }

                Part part = req.getPart("icon");
                if (part != null && part.getSize() > 0) {
                    String originalFileName = part.getSubmittedFileName();
                    if (originalFileName != null && !originalFileName.isEmpty()) {
                        int index = originalFileName.lastIndexOf(".");
                        String ext = originalFileName.substring(index + 1);
                        String fileName = System.currentTimeMillis() + "." + ext;
                        File dir = new File(Constant.DIR + "/category");
                        if (!dir.exists()) dir.mkdirs();
                        File file = new File(dir, fileName);
                        part.write(file.getAbsolutePath());
                        category.setIcon("category/" + fileName);
                    }
                }
                cateService.update(category);
                resp.sendRedirect(req.getContextPath() + "/admin/categories?message=edit_success");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi khi cập nhật danh mục: " + e.getMessage());
                req.setAttribute("category", category);
                req.getRequestDispatcher("/views/admin/edit-category.jsp").forward(req, resp);
            }
        }
    }

    public static void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}
