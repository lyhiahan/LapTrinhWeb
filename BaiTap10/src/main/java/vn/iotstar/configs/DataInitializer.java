package vn.iotstar.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entities.User;
import vn.iotstar.repositories.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdminAccount(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@iotstar.vn";
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setFullName("Hệ Thống Quản Trị Viên");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ROLE_ADMIN");
                admin.setAccountNonLocked(true);
                admin.setEnabled(true);
                userRepository.save(admin);
                System.out.println(">>> Đã khởi tạo tài khoản Quản trị viên mặc định: " + adminEmail + " / admin123 (ROLE_ADMIN)");
            }
        };
    }
}
