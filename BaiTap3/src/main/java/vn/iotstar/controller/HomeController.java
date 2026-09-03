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
@WebServlet(urlPatterns = "/home")
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        IProductService productService = new ProductServiceImpl();
        ICategoryService categoryService = new CategoryServiceImpl();

        List<Product> newestProducts = productService.findNewest(10);
        List<Category> categories = categoryService.findAll();

        req.setAttribute("newestProducts", newestProducts);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher(Constant.Path.HOME).forward(req, resp);
    }
}
