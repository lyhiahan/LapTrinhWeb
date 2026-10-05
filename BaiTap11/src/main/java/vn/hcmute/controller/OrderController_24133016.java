package vn.hcmute.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.hcmute.entity.OrderStatus_24133016;
import vn.hcmute.entity.Order_24133016;
import vn.hcmute.entity.User_24133016;
import vn.hcmute.entity.Video_24133016;
import vn.hcmute.model.CartItem_24133016;
import vn.hcmute.service.IVideoService_24133016;
import vn.hcmute.service.OrderService_24133016;

@Controller
public class OrderController_24133016 {
    private static final String CHECKOUT_TOKEN = "checkoutToken";
    private final OrderService_24133016 orderService;
    private final IVideoService_24133016 videoService;

    public OrderController_24133016(OrderService_24133016 orderService, IVideoService_24133016 videoService) {
        this.orderService = orderService;
        this.videoService = videoService;
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        User_24133016 user = currentUser(session);
        if (user == null) return "redirect:/login";
        Map<String, Integer> cart = CartController_24133016.getCart(session);
        if (cart.isEmpty()) return "redirect:/cart";
        List<CartItem_24133016> items = cartItems(cart);
        if (items.isEmpty()) return "redirect:/cart";
        model.addAttribute("cartItems", items);
        model.addAttribute("cartTotal", items.stream().map(CartItem_24133016::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        model.addAttribute("user", user);
        String checkoutToken = UUID.randomUUID().toString();
        session.setAttribute(CHECKOUT_TOKEN, checkoutToken);
        model.addAttribute("checkoutToken", checkoutToken);
        return "checkout";
    }

    @PostMapping("/checkout/cod")
    public String checkoutCod(@RequestParam String receiverName, @RequestParam String phone,
                              @RequestParam String address,
                              @RequestParam(required = false, defaultValue = "") String note,
                              @RequestParam String checkoutToken,
                              HttpSession session, RedirectAttributes redirect) {
        User_24133016 user = currentUser(session);
        if (user == null) return "redirect:/login";
        synchronized (session) {
            String expected = (String) session.getAttribute(CHECKOUT_TOKEN);
            if (expected == null || !expected.equals(checkoutToken)) {
                redirect.addFlashAttribute("error", "Yêu cầu thanh toán đã hết hạn hoặc đã được xử lý. Vui lòng kiểm tra lịch sử đơn hàng.");
                return "redirect:/cart";
            }
            session.removeAttribute(CHECKOUT_TOKEN);
        }
        String validation = validate(receiverName, phone, address);
        if (validation == null && note != null && note.trim().length() > 500) {
            validation = "Ghi chú không được vượt quá 500 ký tự.";
        }
        if (validation != null) {
            redirect.addFlashAttribute("error", validation);
            return "redirect:/checkout";
        }
        try {
            Map<String, Integer> cart = CartController_24133016.getCart(session);
            Order_24133016 order = orderService.checkoutCod(user.getUsername(), cart,
                    receiverName, phone, address, note);
            session.removeAttribute(CartController_24133016.CART_SESSION_KEY);
            redirect.addFlashAttribute("success", "Đặt hàng COD thành công. Mã đơn của bạn là #" + order.getOrderId() + ".");
            return "redirect:/orders";
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/orders")
    public String history(@RequestParam(required = false) String status,
                          HttpSession session, Model model, RedirectAttributes redirect) {
        User_24133016 user = currentUser(session);
        if (user == null) return "redirect:/login";
        OrderStatus_24133016 selected = null;
        if (status != null && !status.isBlank()) {
            try {
                selected = OrderStatus_24133016.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ex) {
                redirect.addFlashAttribute("error", "Trạng thái lọc không hợp lệ.");
                return "redirect:/orders";
            }
        }
        model.addAttribute("orders", orderService.history(user.getUsername(), selected));
        model.addAttribute("statuses", OrderStatus_24133016.values());
        model.addAttribute("selectedStatus", selected);
        return "order-history";
    }

    private List<CartItem_24133016> cartItems(Map<String, Integer> cart) {
        List<CartItem_24133016> items = new ArrayList<>();
        cart.forEach((id, quantity) -> {
            Video_24133016 product = videoService.findById(id);
            if (product != null && quantity != null && quantity > 0) items.add(new CartItem_24133016(product, quantity));
        });
        return items;
    }

    private User_24133016 currentUser(HttpSession session) {
        Object account = session.getAttribute("account");
        return account instanceof User_24133016 ? (User_24133016) account : null;
    }

    private String validate(String name, String phone, String address) {
        if (name == null || name.isBlank() || name.trim().length() > 100) return "Họ tên người nhận không hợp lệ.";
        if (phone == null || !phone.trim().matches("0[0-9]{9,10}")) return "Số điện thoại phải gồm 10-11 chữ số và bắt đầu bằng 0.";
        if (address == null || address.isBlank() || address.trim().length() > 500) return "Địa chỉ nhận hàng không hợp lệ.";
        return null;
    }
}
