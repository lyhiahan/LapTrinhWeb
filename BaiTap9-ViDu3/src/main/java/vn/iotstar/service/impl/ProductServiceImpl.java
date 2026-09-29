package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.mapper.ProductMapper;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final int MAX_PAGE_SIZE = 100;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper mapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String keyword, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(
            safePage,
            safeSize,
            Sort.by(Sort.Direction.DESC, "id")
        );
        return productRepository.search(keyword == null ? "" : keyword, pageable)
            .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return mapper.toDTO(productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại")));
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto, MultipartFile image) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("ID sản phẩm không được để trống");
        }
        if (productRepository.existsById(dto.getId())) {
            throw new IllegalArgumentException("ID sản phẩm '" + dto.getId() + "' đã tồn tại, vui lòng nhập ID khác");
        }
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));
        Product product = mapper.toEntity(dto);
        product.setId(dto.getId());
        product.setUser(user);

        if (image != null && !image.isEmpty()) {
            CloudinaryUploadResult r = cloudinaryService.upload(image);
            product.setImageUrl(r.url() + "|" + r.publicId());
            // Nếu giao dịch cơ sở dữ liệu bị rollback, dọn dẹp ảnh rác vừa tải lên
            scheduleDeleteOnRollback(r.publicId());
        }

        return mapper.toDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto, MultipartFile image, Long currentUserId, boolean isAdmin) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại"));

        if (!isAdmin && (product.getUser() == null || !product.getUser().getId().equals(currentUserId))) {
            throw new org.springframework.security.access.AccessDeniedException("Bạn không có quyền chỉnh sửa sản phẩm này.");
        }

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());

        String oldImage = product.getImageUrl();
        if (image != null && !image.isEmpty()) {
            CloudinaryUploadResult r = cloudinaryService.upload(image);
            product.setImageUrl(r.url() + "|" + r.publicId());
            // Nếu transaction rollback, dọn dẹp ảnh mới
            scheduleDeleteOnRollback(r.publicId());
            // Chỉ xóa ảnh cũ trên Cloudinary SAU KHI transaction commit thành công
            String oldPublicId = extractPublicId(oldImage);
            if (oldPublicId != null) {
                scheduleDeleteAfterCommit(oldPublicId);
            }
        }

        Product saved = productRepository.save(product);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Long id, Long currentUserId, boolean isAdmin) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại"));

        if (!isAdmin && (product.getUser() == null || !product.getUser().getId().equals(currentUserId))) {
            throw new org.springframework.security.access.AccessDeniedException("Bạn không có quyền xóa sản phẩm này.");
        }

        String oldPublicId = extractPublicId(product.getImageUrl());
        productRepository.delete(product);

        // Chỉ xóa ảnh trên Cloudinary SAU KHI CSDL đã commit xóa thành công
        if (oldPublicId != null) {
            scheduleDeleteAfterCommit(oldPublicId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countProducts() {
        return productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUser(Long userId) {
        return productRepository.countByUserId(userId);
    }

    private String extractPublicId(String imageUrl) {
        if (imageUrl != null && imageUrl.contains("|")) {
            return imageUrl.substring(imageUrl.indexOf('|') + 1);
        }
        return null;
    }

    private void scheduleDeleteAfterCommit(String publicId) {
        if (publicId == null || publicId.isBlank()) return;
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        cloudinaryService.delete(publicId);
                        log.info("Đã xóa ảnh Cloudinary [{}] sau khi commit thành công", publicId);
                    } catch (Exception e) {
                        log.warn("Không thể xóa ảnh Cloudinary [{}] sau commit: {}", publicId, e.getMessage());
                    }
                }
            });
        } else {
            try {
                cloudinaryService.delete(publicId);
            } catch (Exception e) {
                log.warn("Không thể xóa ảnh Cloudinary [{}]: {}", publicId, e.getMessage());
            }
        }
    }

    private void scheduleDeleteOnRollback(String publicId) {
        if (publicId == null || publicId.isBlank()) return;
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status != STATUS_COMMITTED) {
                        try {
                            cloudinaryService.delete(publicId);
                            log.info("Đã dọn dẹp ảnh rác Cloudinary [{}] do transaction rollback", publicId);
                        } catch (Exception e) {
                            log.warn("Không thể dọn ảnh Cloudinary [{}] sau rollback: {}", publicId, e.getMessage());
                        }
                    }
                }
            });
        }
    }
}
