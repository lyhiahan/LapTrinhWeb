package vn.iotstar.controller;
import java.io.IOException;
import java.util.List;
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
@WebServlet(urlPatterns = "/product")
public class ProductController extends HttpServlet {
    private static final int PAGE_SIZE = 6;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        IProductService productService = new ProductServiceImpl();
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
        int totalProducts = productService.count();
        int totalPages = 0;
        List<Product> productList = null;
        if (totalProducts > 0) {
            totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);
            if (page > totalPages) page = totalPages;
            productList = productService.findPaginated(page, PAGE_SIZE);
        } else {
            page = 1;
        }
        req.setAttribute("productList", productList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalProducts", totalProducts);
        req.getRequestDispatcher(Constant.Path.PRODUCT_LIST).forward(req, resp);
    }
}
