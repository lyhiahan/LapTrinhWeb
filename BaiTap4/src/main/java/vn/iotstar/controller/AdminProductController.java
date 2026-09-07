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
@MultipartConfig
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
            Product product = new Product();
            try {
                product.setProductName(req.getParameter("productName"));
                product.setDescription(req.getParameter("description"));

                String priceStr = req.getParameter("price");
                if (priceStr != null && !priceStr.isEmpty()) {
                    product.setPrice(Double.parseDouble(priceStr));
                }

                String cateIdStr = req.getParameter("categoryId");
                if (cateIdStr != null && !cateIdStr.isEmpty()) {
                    Category category = categoryService.findById(Integer.parseInt(cateIdStr));
                    product.setCategory(category);
                }

                product.setCreatedDate(new Date(System.currentTimeMillis()));

                Part part = req.getPart("image");
                if (part != null && part.getSize() > 0) {
                    String originalFileName = part.getSubmittedFileName();
                    if (originalFileName != null && !originalFileName.isEmpty()) {
                        int index = originalFileName.lastIndexOf(".");
                        String ext = originalFileName.substring(index + 1);
                        String fileName = System.currentTimeMillis() + "." + ext;
                        File dir = new File(Constant.DIR + "/product");
                        if (!dir.exists()) dir.mkdirs();
                        File file = new File(dir, fileName);
                        part.write(file.getAbsolutePath());
                        product.setImage("product/" + fileName);
                    }
                }
                productService.insert(product);
                resp.sendRedirect(req.getContextPath() + "/admin/products?message=add_success");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
                List<Category> cateList = categoryService.findAll();
                req.setAttribute("cateList", cateList);
                req.getRequestDispatcher("/views/admin/add-product.jsp").forward(req, resp);
            }

        } else if (url.contains("/admin/product/update")) {
            Product product = new Product();
            try {
                String idStr = req.getParameter("productId");
                if (idStr != null) {
                    product.setProductId(Integer.parseInt(idStr));
                }
                product.setProductName(req.getParameter("productName"));
                product.setDescription(req.getParameter("description"));

                String priceStr = req.getParameter("price");
                if (priceStr != null && !priceStr.isEmpty()) {
                    product.setPrice(Double.parseDouble(priceStr));
                }

                String cateIdStr = req.getParameter("categoryId");
                if (cateIdStr != null && !cateIdStr.isEmpty()) {
                    Category category = categoryService.findById(Integer.parseInt(cateIdStr));
                    product.setCategory(category);
                }

                Part part = req.getPart("image");
                if (part != null && part.getSize() > 0) {
                    String originalFileName = part.getSubmittedFileName();
                    if (originalFileName != null && !originalFileName.isEmpty()) {
                        int index = originalFileName.lastIndexOf(".");
                        String ext = originalFileName.substring(index + 1);
                        String fileName = System.currentTimeMillis() + "." + ext;
                        File dir = new File(Constant.DIR + "/product");
                        if (!dir.exists()) dir.mkdirs();
                        File file = new File(dir, fileName);
                        part.write(file.getAbsolutePath());
                        product.setImage("product/" + fileName);
                    }
                }
                productService.update(product);
                resp.sendRedirect(req.getContextPath() + "/admin/products?message=edit_success");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
                req.setAttribute("product", product);
                List<Category> cateList = categoryService.findAll();
                req.setAttribute("cateList", cateList);
                req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req, resp);
            }
        }
    }
}
