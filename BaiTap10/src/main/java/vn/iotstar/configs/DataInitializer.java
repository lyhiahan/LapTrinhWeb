package vn.iotstar.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entities.User;
import vn.iotstar.repositories.UserRepository;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Value("${app.security.admin.seed-enabled:true}")
    private boolean seedEnabled;

    @Value("${app.security.admin.email:admin@iotstar.vn}")
    private String adminEmail;

    @Value("${app.security.admin.password:admin123}")
    private String adminPassword;

    @Bean
    CommandLineRunner initAdminAccount(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!seedEnabled) {
                log.info("Admin seeding disabled by configuration.");
                return;
            }

            if (adminEmail == null || adminEmail.isBlank() || adminPassword == null || adminPassword.isBlank()) {
                log.warn("Admin email or password not specified in configuration; skipping admin initialization.");
                return;
            }

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setFullName("Hệ Thống Quản Trị Viên");
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole("ROLE_ADMIN");
                admin.setAccountNonLocked(true);
                admin.setEnabled(true);
                userRepository.save(admin);
                log.info("Khởi tạo tài khoản Quản trị viên hệ thống ban đầu: {} [ROLE_ADMIN]", adminEmail);
            }
        };
    }
}
