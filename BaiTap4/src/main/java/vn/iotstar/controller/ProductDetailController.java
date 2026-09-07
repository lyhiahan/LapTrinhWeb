package vn.iotstar.controller;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Product;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/product/detail")
public class ProductDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }
        IProductService productService = new ProductServiceImpl();
        Product product = productService.findById(Integer.parseInt(idStr));
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }
        req.setAttribute("product", product);
        req.getRequestDispatcher(Constant.Path.PRODUCT_DETAIL).forward(req, resp);
    }
}
