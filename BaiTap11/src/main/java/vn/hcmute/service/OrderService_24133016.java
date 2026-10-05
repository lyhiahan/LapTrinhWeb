package vn.hcmute.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hcmute.entity.OrderItem_24133016;
import vn.hcmute.entity.OrderStatus_24133016;
import vn.hcmute.entity.Order_24133016;
import vn.hcmute.entity.User_24133016;
import vn.hcmute.entity.Video_24133016;
import vn.hcmute.repository.OrderRepository_24133016;
import vn.hcmute.repository.UserRepository_24133016;
import vn.hcmute.repository.VideoRepository_24133016;

@Service
public class OrderService_24133016 {
    private final OrderRepository_24133016 orderRepository;
    private final VideoRepository_24133016 videoRepository;
    private final UserRepository_24133016 userRepository;

    public OrderService_24133016(OrderRepository_24133016 orderRepository,
                                 VideoRepository_24133016 videoRepository,
                                 UserRepository_24133016 userRepository) {
        this.orderRepository = orderRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order_24133016 checkoutCod(String username, Map<String, Integer> cart,
                                     String receiverName, String phone, String address, String note) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng đang trống.");
        }
        User_24133016 user = userRepository.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại."));

        Order_24133016 order = new Order_24133016();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus_24133016.NEW);
        order.setPaymentMethod("COD");
        order.setReceiverName(receiverName.trim());
        order.setPhone(phone.trim());
        order.setAddress(address.trim());
        order.setNote(note == null ? null : note.trim());

        BigDecimal total = BigDecimal.ZERO;
        // Khóa sản phẩm theo cùng một thứ tự để tránh deadlock khi có nhiều checkout đồng thời.
        List<String> productIds = new ArrayList<>(cart.keySet());
        Collections.sort(productIds);
        for (String productId : productIds) {
            Video_24133016 product = videoRepository.findByIdForUpdate(productId);
            Integer cartQuantity = cart.get(productId);
            int quantity = cartQuantity == null ? 0 : cartQuantity;
            if (product == null || !Boolean.TRUE.equals(product.getActive())) {
                throw new IllegalArgumentException("Sản phẩm " + productId + " không còn được bán.");
            }
            int stock = product.getStock() == null ? 0 : product.getStock();
            if (quantity < 1 || quantity > stock) {
                throw new IllegalArgumentException("Số lượng “" + product.getTitle() + "” chỉ còn " + stock + ".");
            }
            BigDecimal price = product.getPrice() == null ? BigDecimal.ZERO : product.getPrice();
            OrderItem_24133016 item = new OrderItem_24133016();
            item.setVideoId(product.getVideoId());
            item.setProductName(product.getTitle());
            item.setPoster(product.getPoster());
            item.setUnitPrice(price);
            item.setQuantity(quantity);
            order.addItem(item);
            total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
            product.setStock(stock - quantity);
        }
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order_24133016> history(String username, OrderStatus_24133016 status) {
        return status == null
                ? orderRepository.findHistory(username)
                : orderRepository.findHistoryByStatus(username, status);
    }
}
