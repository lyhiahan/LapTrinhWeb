package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/product")
public class ProductController extends HttpServlet {
    private static final int PAGE_SIZE = 8;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        IProductService productService = new ProductServiceImpl();
        ICategoryService categoryService = new CategoryServiceImpl();

        List<Category> categories = categoryService.findAll();
        req.setAttribute("categories", categories);

        int page = 1;
        String pageStr = req.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        String cateIdStr = req.getParameter("cateId");
        int totalProducts = 0;
        int totalPages = 0;
        List<Product> products = null;

        if (cateIdStr != null && !cateIdStr.trim().isEmpty()) {
            try {
                int cateId = Integer.parseInt(cateIdStr.trim());
                req.setAttribute("selectedCateId", cateId);
                totalProducts = productService.countByCategoryId(cateId);
                if (totalProducts > 0) {
                    totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);
                    if (page > totalPages) page = totalPages;
                    products = productService.findPaginatedByCategoryId(cateId, page, PAGE_SIZE);
                }
            } catch (NumberFormatException e) {
                totalProducts = productService.count();
                if (totalProducts > 0) {
                    totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);
                    if (page > totalPages) page = totalPages;
                    products = productService.findPaginated(page, PAGE_SIZE);
                }
            }
        } else {
            totalProducts = productService.count();
            if (totalProducts > 0) {
                totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);
                if (page > totalPages) page = totalPages;
                products = productService.findPaginated(page, PAGE_SIZE);
            }
        }

        req.setAttribute("products", products);
        req.setAttribute("productList", products);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalProducts", totalProducts);

        req.getRequestDispatcher(Constant.Path.PRODUCT_LIST).forward(req, resp);
    }
}
