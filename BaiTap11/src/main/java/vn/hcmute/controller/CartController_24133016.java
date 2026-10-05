package vn.hcmute.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.hcmute.entity.Video_24133016;
import vn.hcmute.model.CartItem_24133016;
import vn.hcmute.service.IVideoService_24133016;

@Controller
@RequestMapping("/cart")
public class CartController_24133016 {
    public static final String CART_SESSION_KEY = "cart";
    private static final int MAX_PER_ITEM = 99;
    private final IVideoService_24133016 videoService;

    public CartController_24133016(IVideoService_24133016 videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<CartItem_24133016> items = buildItems(getCart(session));
        BigDecimal total = items.stream().map(CartItem_24133016::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("cartItems", items);
        model.addAttribute("cartTotal", total);
        model.addAttribute("cartCount", itemCount(getCart(session)));
        return "cart";
    }

    @PostMapping("/add/{videoId}")
    public String add(@PathVariable String videoId,
                      @RequestParam(defaultValue = "1") int quantity,
                      @RequestParam(defaultValue = "/home") String returnUrl,
                      HttpSession session, RedirectAttributes redirect) {
        Video_24133016 product = videoService.findById(videoId);
        if (product == null || !Boolean.TRUE.equals(product.getActive())) {
            redirect.addFlashAttribute("error", "Sản phẩm không tồn tại hoặc đã ngừng bán.");
            return safeRedirect(returnUrl);
        }
        int stock = product.getStock() == null ? 0 : product.getStock();
        Map<String, Integer> cart = getCart(session);
        long desired = (long) cart.getOrDefault(videoId, 0) + Math.max(1, quantity);
        int allowed = Math.min(stock, MAX_PER_ITEM);
        if (allowed < 1) {
            redirect.addFlashAttribute("error", "Sản phẩm đã hết hàng.");
        } else {
            cart.put(videoId, (int) Math.min(desired, allowed));
            session.setAttribute(CART_SESSION_KEY, cart);
            redirect.addFlashAttribute("success", desired > allowed
                    ? "Đã điều chỉnh về số lượng tối đa có thể mua: " + allowed + "."
                    : "Đã thêm sản phẩm vào giỏ hàng.");
        }
        return safeRedirect(returnUrl);
    }

    @PostMapping("/update/{videoId}")
    public String update(@PathVariable String videoId, @RequestParam int quantity,
                         HttpSession session, RedirectAttributes redirect) {
        Map<String, Integer> cart = getCart(session);
        if (quantity <= 0) {
            cart.remove(videoId);
            redirect.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng.");
        } else {
            Video_24133016 product = videoService.findById(videoId);
            if (product == null || !Boolean.TRUE.equals(product.getActive())) {
                cart.remove(videoId);
                redirect.addFlashAttribute("error", "Sản phẩm không còn được bán và đã được xóa khỏi giỏ.");
            } else {
                int allowed = Math.min(product.getStock() == null ? 0 : product.getStock(), MAX_PER_ITEM);
                if (allowed < 1) {
                    cart.remove(videoId);
                    redirect.addFlashAttribute("error", "Sản phẩm đã hết hàng và được xóa khỏi giỏ.");
                } else {
                    int newQuantity = Math.min(quantity, allowed);
                    cart.put(videoId, newQuantity);
                    redirect.addFlashAttribute("success", newQuantity < quantity
                            ? "Số lượng được điều chỉnh về giới hạn " + allowed + "."
                            : "Đã cập nhật số lượng.");
                }
            }
        }
        session.setAttribute(CART_SESSION_KEY, cart);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{videoId}")
    public String remove(@PathVariable String videoId, HttpSession session, RedirectAttributes redirect) {
        getCart(session).remove(videoId);
        redirect.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng.");
        return "redirect:/cart";
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Integer> getCart(HttpSession session) {
        Object value = session.getAttribute(CART_SESSION_KEY);
        if (value instanceof Map<?, ?>) return (Map<String, Integer>) value;
        Map<String, Integer> cart = new LinkedHashMap<>();
        session.setAttribute(CART_SESSION_KEY, cart);
        return cart;
    }

    public static int itemCount(Map<String, Integer> cart) {
        return cart.values().stream().filter(q -> q != null && q > 0).mapToInt(Integer::intValue).sum();
    }

    private List<CartItem_24133016> buildItems(Map<String, Integer> cart) {
        List<CartItem_24133016> items = new ArrayList<>();
        cart.forEach((id, quantity) -> {
            Video_24133016 product = videoService.findById(id);
            if (product != null && quantity != null && quantity > 0) {
                items.add(new CartItem_24133016(product, quantity));
            }
        });
        return items;
    }

    private String safeRedirect(String returnUrl) {
        return "redirect:" + (returnUrl != null && returnUrl.startsWith("/") && !returnUrl.startsWith("//")
                ? returnUrl : "/home");
    }
}
