package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
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
import vn.iotstar.entity.Product;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 20
)
@WebServlet(urlPatterns = { "/admin/products", "/admin/product/add", "/admin/product/insert",
        "/admin/product/edit", "/admin/product/update", "/admin/product/delete" })
public class AdminProductController extends HttpServlet {

    IProductService productService = new ProductServiceImpl();
    ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        if (url.contains("/admin/products")) {
            try {
                List<Product> productList = productService.findAll();
                req.setAttribute("productList", productList);
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Không thể hiển thị danh sách do lỗi CSDL: " + e.getMessage());
            }
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/list-product.jsp");
            dispatcher.forward(req, resp);
        } else if (url.contains("/admin/product/add")) {
            List<Category> cateList = categoryService.findAll();
            req.setAttribute("cateList", cateList);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/add-product.jsp");
            dispatcher.forward(req, resp);
        } else if (url.contains("/admin/product/edit")) {
            String id = req.getParameter("id");
            Product product = productService.findById(Integer.parseInt(id));
            List<Category> cateList = categoryService.findAll();
            req.setAttribute("product", product);
            req.setAttribute("cateList", cateList);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/edit-product.jsp");
            dispatcher.forward(req, resp);
        } else if (url.contains("/admin/product/delete")) {
            String id = req.getParameter("id");
            try {
                productService.delete(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("/admin/product/insert")) {
            String productName = req.getParameter("productName") != null ? req.getParameter("productName").trim() : "";
            String description = req.getParameter("description") != null ? req.getParameter("description").trim() : "";
            String priceStr = req.getParameter("price") != null ? req.getParameter("price").trim() : "";
            String cateIdStr = req.getParameter("categoryId") != null ? req.getParameter("categoryId").trim() : "";

            // Server-side validation
            if (productName.isEmpty() || productName.length() < 2 || productName.length() > 255) {
                forwardWithAddError(req, resp, "Tên sản phẩm không hợp lệ (từ 2 đến 255 ký tự)!");
                return;
            }

            double price = 0;
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    forwardWithAddError(req, resp, "Đơn giá sản phẩm phải lớn hơn 0!");
                    return;
                }
            } catch (Exception e) {
                forwardWithAddError(req, resp, "Đơn giá sản phẩm không hợp lệ!");
                return;
            }

            int cateId = 0;
            try {
                cateId = Integer.parseInt(cateIdStr);
            } catch (Exception e) {
                forwardWithAddError(req, resp, "Vui lòng chọn một danh mục hợp lệ!");
                return;
            }

            Category category = categoryService.findById(cateId);
            if (category == null) {
                forwardWithAddError(req, resp, "Danh mục đã chọn không tồn tại!");
                return;
            }

            Part part = req.getPart("image");
            if (part == null || part.getSize() == 0) {
                forwardWithAddError(req, resp, "Vui lòng chọn hình ảnh đại diện cho sản phẩm!");
                return;
            }

            String originalFileName = part.getSubmittedFileName();
            String ext = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
            }
            if (!ext.matches("^(jpg|jpeg|png|webp|gif)$")) {
                forwardWithAddError(req, resp, "Định dạng file ảnh không hợp lệ (chỉ chấp nhận JPG, PNG, WEBP, GIF)!");
                return;
            }

            Product product = new Product();
            try {
                product.setProductName(productName);
                product.setDescription(description);
                product.setPrice(price);
                product.setCategory(category);
                product.setCreatedDate(new Date(System.currentTimeMillis()));

                String fileName = System.currentTimeMillis() + "." + ext;
                File dir = new File(Constant.DIR + "/product");
                if (!dir.exists()) dir.mkdirs();
                File file = new File(dir, fileName);
                part.write(file.getAbsolutePath());
                product.setImage("product/" + fileName);

                productService.insert(product);
                resp.sendRedirect(req.getContextPath() + "/admin/products?message=add_success");
            } catch (Exception e) {
                e.printStackTrace();
                forwardWithAddError(req, resp, "Lỗi khi lưu sản phẩm vào hệ thống: " + e.getMessage());
            }
        } else if (url.contains("/admin/product/update")) {
            String idStr = req.getParameter("productId");
            String productName = req.getParameter("productName") != null ? req.getParameter("productName").trim() : "";
            String description = req.getParameter("description") != null ? req.getParameter("description").trim() : "";
            String priceStr = req.getParameter("price") != null ? req.getParameter("price").trim() : "";
            String cateIdStr = req.getParameter("categoryId") != null ? req.getParameter("categoryId").trim() : "";

            int productId = 0;
            try {
                productId = Integer.parseInt(idStr);
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/admin/products");
                return;
            }

            Product product = productService.findById(productId);
            if (product == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/products");
                return;
            }

            if (productName.isEmpty() || productName.length() < 2 || productName.length() > 255) {
                forwardWithEditError(req, resp, product, "Tên sản phẩm không hợp lệ (từ 2 đến 255 ký tự)!");
                return;
            }

            double price = 0;
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    forwardWithEditError(req, resp, product, "Đơn giá sản phẩm phải lớn hơn 0!");
                    return;
                }
            } catch (Exception e) {
                forwardWithEditError(req, resp, product, "Đơn giá sản phẩm không hợp lệ!");
                return;
            }

            int cateId = 0;
            try {
                cateId = Integer.parseInt(cateIdStr);
            } catch (Exception e) {
                forwardWithEditError(req, resp, product, "Vui lòng chọn một danh mục hợp lệ!");
                return;
            }

            Category category = categoryService.findById(cateId);
            if (category == null) {
                forwardWithEditError(req, resp, product, "Danh mục đã chọn không tồn tại!");
                return;
            }

            try {
                product.setProductName(productName);
                product.setDescription(description);
                product.setPrice(price);
                product.setCategory(category);

                Part part = req.getPart("image");
                if (part != null && part.getSize() > 0) {
                    String originalFileName = part.getSubmittedFileName();
                    String ext = "";
                    if (originalFileName != null && originalFileName.contains(".")) {
                        ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
                    }
                    if (!ext.matches("^(jpg|jpeg|png|webp|gif)$")) {
                        forwardWithEditError(req, resp, product, "Định dạng file ảnh không hợp lệ (chỉ chấp nhận JPG, PNG, WEBP, GIF)!");
                        return;
                    }
                    String fileName = System.currentTimeMillis() + "." + ext;
                    File dir = new File(Constant.DIR + "/product");
                    if (!dir.exists()) dir.mkdirs();
                    File file = new File(dir, fileName);
                    part.write(file.getAbsolutePath());
                    product.setImage("product/" + fileName);
                }

                productService.update(product);
                resp.sendRedirect(req.getContextPath() + "/admin/products?message=edit_success");
            } catch (Exception e) {
                e.printStackTrace();
                forwardWithEditError(req, resp, product, "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            }
        }
    }

    private void forwardWithAddError(HttpServletRequest req, HttpServletResponse resp, String errorMsg)
            throws ServletException, IOException {
        req.setAttribute("error", errorMsg);
        List<Category> cateList = categoryService.findAll();
        req.setAttribute("cateList", cateList);
        req.getRequestDispatcher("/views/admin/add-product.jsp").forward(req, resp);
    }

    private void forwardWithEditError(HttpServletRequest req, HttpServletResponse resp, Product product, String errorMsg)
            throws ServletException, IOException {
        req.setAttribute("error", errorMsg);
        req.setAttribute("product", product);
        List<Category> cateList = categoryService.findAll();
        req.setAttribute("cateList", cateList);
        req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req, resp);
    }
}
