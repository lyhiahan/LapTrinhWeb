package vn.hcmute.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import vn.hcmute.entity.OrderStatus_24133016;
import vn.hcmute.entity.Order_24133016;
import vn.hcmute.entity.User_24133016;
import vn.hcmute.entity.Video_24133016;
import vn.hcmute.repository.OrderRepository_24133016;
import vn.hcmute.repository.UserRepository_24133016;
import vn.hcmute.repository.VideoRepository_24133016;

class OrderService_24133016Test {
    @Mock private OrderRepository_24133016 orderRepository;
    @Mock private VideoRepository_24133016 videoRepository;
    @Mock private UserRepository_24133016 userRepository;
    private OrderService_24133016 service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new OrderService_24133016(orderRepository, videoRepository, userRepository);
        when(userRepository.findById("user1")).thenReturn(Optional.of(new User_24133016()));
        when(orderRepository.save(any(Order_24133016.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void checkoutCodCreatesOrderAndDecreasesStock() {
        Video_24133016 video = product("VD001", "Khóa học", "125000", 10);
        when(videoRepository.findByIdForUpdate("VD001")).thenReturn(video);
        Map<String, Integer> cart = new LinkedHashMap<>();
        cart.put("VD001", 2);

        service.checkoutCod("user1", cart, "Nguyễn Văn A", "0901234567", "TP.HCM", "");

        ArgumentCaptor<Order_24133016> captor = ArgumentCaptor.forClass(Order_24133016.class);
        verify(orderRepository).save(captor.capture());
        Order_24133016 order = captor.getValue();
        assertEquals(OrderStatus_24133016.NEW, order.getStatus());
        assertEquals("COD", order.getPaymentMethod());
        assertEquals(new BigDecimal("250000"), order.getTotalAmount());
        assertEquals(1, order.getItems().size());
        assertEquals(8, video.getStock());
    }

    @Test
    void checkoutCodRejectsQuantityAboveStock() {
        Video_24133016 video = product("VD001", "Khóa học", "125000", 1);
        when(videoRepository.findByIdForUpdate("VD001")).thenReturn(video);

        assertThrows(IllegalArgumentException.class,
                () -> service.checkoutCod("user1", Map.of("VD001", 2), "A", "0901234567", "TP.HCM", ""));

        verify(orderRepository, never()).save(any());
        assertEquals(1, video.getStock());
    }

    private Video_24133016 product(String id, String title, String price, int stock) {
        Video_24133016 video = new Video_24133016();
        video.setVideoId(id);
        video.setTitle(title);
        video.setPrice(new BigDecimal(price));
        video.setStock(stock);
        video.setActive(true);
        return video;
    }
}
