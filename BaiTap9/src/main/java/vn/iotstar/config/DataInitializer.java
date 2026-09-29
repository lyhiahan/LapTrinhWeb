package vn.iotstar.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               @Value("${app.seed.enabled:true}") boolean seedEnabled,
                               @Value("${ADMIN_EMAIL:admin@gmail.com}") String adminEmail,
                               @Value("${ADMIN_PASSWORD:}") String adminPassword,
                               @Value("${USER_PASSWORD:123456}") String userPassword) {
        return args -> {
            // Giới hạn khởi tạo dữ liệu mẫu cho môi trường thực hành/dev (bật/tắt qua cấu hình)
            if (!seedEnabled) {
                log.info("Bỏ qua khởi tạo dữ liệu mẫu do app.seed.enabled=false");
                return;
            }

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

            // 1. Chỉ tạo tài khoản mẫu user01 khi chưa tồn tại (tránh ghi đè thông tin người dùng đã chỉnh sửa)
            if (userRepository.findByUsername("user01").isEmpty()) {
                User user = User.builder()
                        .username("user01")
                        .email("user01@gmail.com")
                        .password(passwordEncoder.encode(userPassword))
                        .fullName("Lý Gia Hân")
                        .images("/images/user.png")
                        .role(userRole)
                        .enabled(true)
                        .build();
                userRepository.save(user);
                log.info("Khởi tạo thành công tài khoản mẫu user01.");
            }

            // 2. Yêu cầu cấu hình mật khẩu ADMIN rõ ràng, cảnh báo nếu thiếu cấu hình
            if (userRepository.findByUsername("admin").isEmpty()) {
                String effectiveAdminPassword = adminPassword;
                if (effectiveAdminPassword == null || effectiveAdminPassword.isBlank()) {
                    log.warn("CẢNH BÁO BẢO MẬT: ADMIN_PASSWORD chưa được cấu hình! Tạm thời sử dụng mật khẩu mặc định cho môi trường thực hành. Vui lòng cấu hình ADMIN_PASSWORD trong file .env trước khi triển khai!");
                    effectiveAdminPassword = "Admin@ChangeMe123";
                }

                User admin = User.builder()
                        .username("admin")
                        .email(adminEmail.toLowerCase())
                        .password(passwordEncoder.encode(effectiveAdminPassword))
                        .fullName("System Administrator")
                        .images("/images/avatar-default.png")
                        .role(adminRole)
                        .enabled(true)
                        .build();
                userRepository.save(admin);
                log.info("Khởi tạo thành công tài khoản quản trị admin.");
            }
        };
    }
}