package vn.iotstar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.mapper.ProductMapper;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceSecurityTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductMapper mapper;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ProductServiceImpl productService;

    private User owner;
    private Product product;
    private ProductDTO updateDto;

    @BeforeEach
    void setUp() {
        owner = User.builder()
            .id(10L)
            .username("owner_user")
            .build();

        product = Product.builder()
            .id(100L)
            .name("Bông hoa")
            .description("Mô tả hoa")
            .price(BigDecimal.valueOf(50000))
            .user(owner)
            .imageUrl("https://cloud/img.jpg|public_123")
            .build();

        updateDto = new ProductDTO();
        updateDto.setName("Bông hoa đã sửa");
        updateDto.setDescription("Mô tả mới");
        updateDto.setPrice(BigDecimal.valueOf(60000));
    }

    @Test
    @DisplayName("Chủ sở hữu sản phẩm có thể cập nhật sản phẩm của mình")
    void updateProduct_Owner_Success() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(mapper.toDTO(any(Product.class))).thenReturn(updateDto);

        assertDoesNotThrow(() ->
            productService.update(100L, updateDto, null, 10L, false)
        );

        verify(productRepository, times(1)).save(product);
        assertEquals("Bông hoa đã sửa", product.getName());
    }

    @Test
    @DisplayName("Admin có thể cập nhật sản phẩm của người khác")
    void updateProduct_Admin_Success() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(mapper.toDTO(any(Product.class))).thenReturn(updateDto);

        assertDoesNotThrow(() ->
            productService.update(100L, updateDto, null, 999L, true)
        );

        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Người dùng khác không có quyền sửa sản phẩm, ném AccessDeniedException")
    void updateProduct_NonOwner_ThrowsAccessDeniedException() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () ->
            productService.update(100L, updateDto, null, 99L, false)
        );

        assertTrue(ex.getMessage().contains("không có quyền chỉnh sửa"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Người dùng khác không có quyền xóa sản phẩm, ném AccessDeniedException")
    void deleteProduct_NonOwner_ThrowsAccessDeniedException() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () ->
            productService.delete(100L, 99L, false)
        );

        assertTrue(ex.getMessage().contains("không có quyền xóa"));
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    @DisplayName("Chủ sở hữu hoặc Admin có thể xóa sản phẩm")
    void deleteProduct_Owner_Success() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() ->
            productService.delete(100L, 10L, false)
        );

        verify(productRepository, times(1)).delete(product);
    }
}
