package vn.iotstar.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
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

    @Override
    public void run(String... args) {
        Role roleUser = ensureRole("user");
        Role roleAdmin = ensureRole("admin");
        Role roleUserSpring = ensureRole("ROLE_USER");
        Role roleAdminSpring = ensureRole("ROLE_ADMIN");

        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                .username("admin")
                .email("admin@iotstar.vn")
                .fullName("Quản trị viên Hệ thống")
                .password(passwordEncoder.encode("123456"))
                .enabled(true)
                .role(roleAdminSpring)
                .build();
            userRepository.save(admin);
            log.info(">>> Đã khởi tạo tài khoản admin: admin / 123456");
        }

        if (!userRepository.existsByUsername("user")) {
            User user = User.builder()
                .username("user")
                .email("user@iotstar.vn")
                .fullName("Người dùng mẫu")
                .password(passwordEncoder.encode("123456"))
                .enabled(true)
                .role(roleUserSpring)
                .build();
            userRepository.save(user);
            log.info(">>> Đã khởi tạo tài khoản user: user / 123456");
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
