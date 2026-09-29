package vn.iotstar.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @org.springframework.beans.factory.annotation.Value("${app.seed.demo-accounts:false}")
    private boolean seedDemoAccounts = false;

    @org.springframework.beans.factory.annotation.Value("${app.seed.default-password:}")
    private String defaultPassword = "";

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Migration an toàn cho bảng users trên CSDL cũ (thêm email_verified và locked nếu chưa có)
        ensureSchemaMigration();

        // 2. Chuẩn hóa chỉ giữ ROLE_USER và ROLE_ADMIN chuẩn Spring Security
        Role roleUser = ensureRole("ROLE_USER");
        Role roleAdmin = ensureRole("ROLE_ADMIN");

        // Di chuyển các tài khoản dùng role cũ "user", "admin" sang "ROLE_USER", "ROLE_ADMIN" và xóa role dư thừa
        roleRepository.findByName("user").ifPresent(oldUserRole -> {
            userRepository.updateUserRole(oldUserRole, roleUser);
            roleRepository.delete(oldUserRole);
            log.info(">>> Đã di chuyển người dùng từ role cũ [user] sang [ROLE_USER] và dọn dẹp role cũ.");
        });

        roleRepository.findByName("admin").ifPresent(oldAdminRole -> {
            userRepository.updateUserRole(oldAdminRole, roleAdmin);
            roleRepository.delete(oldAdminRole);
            log.info(">>> Đã di chuyển người dùng từ role cũ [admin] sang [ROLE_ADMIN] và dọn dẹp role cũ.");
        });

        // 3. Tự động kích hoạt email cho các tài khoản cũ đang hoạt động
        try {
            int migratedCount = userRepository.migrateLegacyActiveUsers();
            if (migratedCount > 0) {
                log.info(">>> Đã đồng bộ trạng thái xác minh email cho {} tài khoản hoạt động sẵn có.", migratedCount);
            }
        } catch (Exception e) {
            log.warn("Bỏ qua đồng bộ trạng thái user: {}", e.getMessage());
        }

        // 4. Khởi tạo tài khoản demo nếu được bật qua cấu hình
        if (!seedDemoAccounts) {
            log.info(">>> Môi trường không bật seed demo accounts (SEED_DEMO_ACCOUNTS=false). Bỏ qua tạo admin/user mặc định.");
            return;
        }

        String initialPassword = (defaultPassword != null && !defaultPassword.isBlank()) ? defaultPassword : "DemoPassword@123";

        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                .username("admin")
                .email("admin@iotstar.vn")
                .fullName("Quản trị viên Hệ thống")
                .password(passwordEncoder.encode(initialPassword))
                .enabled(true)
                .emailVerified(true)
                .locked(false)
                .role(roleAdmin)
                .build();
            userRepository.save(admin);
            log.info(">>> [DEMO] Đã khởi tạo tài khoản quản trị viên mẫu: admin");
        }

        if (!userRepository.existsByUsername("user")) {
            User user = User.builder()
                .username("user")
                .email("user@iotstar.vn")
                .fullName("Người dùng mẫu")
                .password(passwordEncoder.encode(initialPassword))
                .enabled(true)
                .emailVerified(true)
                .locked(false)
                .role(roleUser)
                .build();
            userRepository.save(user);
            log.info(">>> [DEMO] Đã khởi tạo tài khoản người dùng mẫu: user");
        }
    }

    private void ensureSchemaMigration() {
        try {
            jdbcTemplate.execute("""
                IF NOT EXISTS (
                    SELECT 1 FROM sys.columns 
                    WHERE object_id = OBJECT_ID(N'users') AND name = 'email_verified'
                )
                BEGIN
                    ALTER TABLE users ADD email_verified BIT NOT NULL CONSTRAINT DF_users_email_verified DEFAULT 1;
                END
            """);

            jdbcTemplate.execute("""
                IF NOT EXISTS (
                    SELECT 1 FROM sys.columns 
                    WHERE object_id = OBJECT_ID(N'users') AND name = 'locked'
                )
                BEGIN
                    ALTER TABLE users ADD locked BIT NOT NULL CONSTRAINT DF_users_locked DEFAULT 0;
                END
            """);
            log.info(">>> Kiểm tra và đảm bảo schema migration các cột email_verified, locked thành công.");
        } catch (Exception e) {
            log.warn("Không thể thực hiện tự động migration schema (có thể không phải SQL Server hoặc không đủ quyền DDL): {}", e.getMessage());
        }
    }

    private Role ensureRole(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = Role.builder().name(roleName).build();
            Role saved = roleRepository.save(role);
            log.info(">>> Khởi tạo sẵn Role trong CSDL: {}", roleName);
            return saved;
        });
    }
}
