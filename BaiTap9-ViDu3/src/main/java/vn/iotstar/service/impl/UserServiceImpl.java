package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import vn.iotstar.dto.UserDTO;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.mapper.UserMapper;
import vn.iotstar.repository.OtpTokenRepository;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.CloudinaryService;
import vn.iotstar.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final OtpTokenRepository otpTokenRepository;
    private final CloudinaryService cloudinaryService;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${app.security.default-user-password:123456}")
    private String defaultUserPassword = "123456";

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(String keyword, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        Pageable pageable = PageRequest.of(
            safePage,
            safeSize,
            Sort.by(Sort.Direction.DESC, "id")
        );
        return userRepository.search(keyword == null ? "" : keyword, pageable)
            .map(user -> {
                UserDTO dto = mapper.toDTO(user);
                dto.setProductCount(userRepository.countProductsByUserId(user.getId()));
                return dto;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));
        UserDTO dto = mapper.toDTO(user);
        dto.setProductCount(userRepository.countProductsByUserId(id));
        return dto;
    }

    @Override
    @Transactional
    public UserDTO create(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new IllegalArgumentException("Username đã tồn tại");
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("Email đã tồn tại");
        User user = mapper.toEntity(dto);
        Role role = resolveRole(dto.getRoleName());
        user.setRole(role);
        String pass = (defaultUserPassword != null && !defaultUserPassword.isBlank()) 
            ? defaultUserPassword 
            : java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        user.setPassword(passwordEncoder.encode(pass));
        user.setEnabled(dto.isEnabled());
        user.setEmailVerified(true);
        user.setLocked(!dto.isEnabled());
        return mapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));
        if (!user.getEmail().equalsIgnoreCase(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("Email đã tồn tại");
        if (!user.getUsername().equalsIgnoreCase(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername()))
            throw new IllegalArgumentException("Username đã tồn tại");
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setEnabled(dto.isEnabled());
        user.setLocked(!dto.isEnabled());
        if (dto.getRoleName() != null && !dto.getRoleName().isBlank()) {
            Role role = resolveRole(dto.getRoleName());
            user.setRole(role);
        }
        return mapper.toDTO(userRepository.save(user));
    }

    private Role resolveRole(String roleName) {
        String target = (roleName == null || roleName.isBlank()) ? "ROLE_USER" : roleName.trim();
        if (!target.toUpperCase().startsWith("ROLE_")) {
            target = "ROLE_" + target.toUpperCase();
        }
        String finalTarget = target;
        return roleRepository.findByName(finalTarget)
            .orElseGet(() -> roleRepository.save(Role.builder().name(finalTarget).build()));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));
        java.util.List<vn.iotstar.entity.Product> userProducts = productRepository.findByUserId(id);
        java.util.List<String> imagePublicIds = new java.util.ArrayList<>();
        for (vn.iotstar.entity.Product p : userProducts) {
            String image = p.getImageUrl();
            if (image != null && image.contains("|")) {
                imagePublicIds.add(image.substring(image.indexOf('|') + 1));
            }
        }
        productRepository.deleteByUserId(id);
        otpTokenRepository.deleteByEmail(user.getEmail());
        userRepository.delete(user);

        // Chỉ dọn dẹp các ảnh trên Cloudinary SAU KHI CSDL đã commit xóa thành công
        if (!imagePublicIds.isEmpty()) {
            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        for (String publicId : imagePublicIds) {
                            try {
                                cloudinaryService.delete(publicId);
                                log.info("Đã xóa ảnh Cloudinary [{}] của user [{}] sau commit", publicId, id);
                            } catch (Exception e) {
                                log.warn("Không thể xóa ảnh Cloudinary [{}] của user [{}]: {}", publicId, id, e.getMessage());
                            }
                        }
                    }
                });
            } else {
                for (String publicId : imagePublicIds) {
                    try {
                        cloudinaryService.delete(publicId);
                    } catch (Exception e) {
                        log.warn("Không thể xóa ảnh Cloudinary [{}] của user [{}]: {}", publicId, id, e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countProducts(Long userId) {
        return userRepository.countProductsByUserId(userId);
    }
}
