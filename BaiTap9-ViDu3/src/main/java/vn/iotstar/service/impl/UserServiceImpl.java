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

        String pass;
        if (dto.getInitialPassword() != null && !dto.getInitialPassword().isBlank()) {
            if (dto.getInitialPassword().trim().length() < 6) {
                throw new IllegalArgumentException("Mật khẩu ban đầu phải có ít nhất 6 ký tự");
            }
            pass = dto.getInitialPassword().trim();
        } else if (defaultUserPassword != null && !defaultUserPassword.isBlank()) {
            pass = defaultUserPassword.trim();
        } else {
            pass = generateSecurePassword();
        }

        user.setPassword(passwordEncoder.encode(pass));
        user.setEnabled(dto.isEnabled());
        user.setEmailVerified(true);
        user.setLocked(!dto.isEnabled());
        UserDTO savedDto = mapper.toDTO(userRepository.save(user));
        savedDto.setInitialPassword(pass);
        return savedDto;
    }

    private String generateSecurePassword() {
        String upper = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String lower = "abcdefghijkmnpqrstuvwxyz";
        String digits = "23456789";
        String special = "@#$!";
        String all = upper + lower + digits + special;
        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder();
        sb.append(upper.charAt(random.nextInt(upper.length())));
        sb.append(lower.charAt(random.nextInt(lower.length())));
        sb.append(digits.charAt(random.nextInt(digits.length())));
        sb.append(special.charAt(random.nextInt(special.length())));
        for (int i = 4; i < 10; i++) {
            sb.append(all.charAt(random.nextInt(all.length())));
        }
        java.util.List<Character> list = new java.util.ArrayList<>();
        for (char c : sb.toString().toCharArray()) list.add(c);
        java.util.Collections.shuffle(list, random);
        StringBuilder res = new StringBuilder();
        for (char c : list) res.append(c);
        return res.toString();
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        return update(id, dto, null);
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO dto, Long currentUserId) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        // Ràng buộc nghiệp vụ: Không cho phép tự khóa tài khoản hoặc tự hạ quyền quản trị
        if (currentUserId != null && currentUserId.equals(id)) {
            if (!dto.isEnabled()) {
                throw new IllegalArgumentException("Không thể tự khóa tài khoản của chính mình.");
            }
            if (dto.getRoleName() != null && !dto.getRoleName().isBlank()) {
                String reqRole = dto.getRoleName().trim().toUpperCase();
                if (!reqRole.startsWith("ROLE_")) {
                    reqRole = "ROLE_" + reqRole;
                }
                if (!reqRole.equals("ROLE_ADMIN")) {
                    throw new IllegalArgumentException("Không thể tự hạ vai trò Quản trị viên của chính mình.");
                }
            }
        }

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
        String target = (roleName == null || roleName.isBlank()) ? "ROLE_USER" : roleName.trim().toUpperCase();
        if (!target.startsWith("ROLE_")) {
            target = "ROLE_" + target;
        }
        if (!target.equals("ROLE_USER") && !target.equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("Vai trò không hợp lệ: [" + roleName + "]. Hệ thống chỉ chấp nhận ROLE_USER hoặc ROLE_ADMIN.");
        }
        final String finalTarget = target;
        return roleRepository.findByName(finalTarget)
            .orElseThrow(() -> new IllegalStateException("Vai trò [" + finalTarget + "] chưa được cấu hình sẵn trong hệ thống."));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        delete(id, null);
    }

    @Override
    @Transactional
    public void delete(Long id, Long currentUserId) {
        if (currentUserId != null && currentUserId.equals(id)) {
            throw new IllegalArgumentException("Không thể tự xóa tài khoản đang đăng nhập.");
        }
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
