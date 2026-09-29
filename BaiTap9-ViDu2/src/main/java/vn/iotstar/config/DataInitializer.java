package vn.iotstar.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Slf4j
@Component
@Profile({"dev", "test", "default"})
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() ->
            roleRepository.save(Role.builder().name("ROLE_USER").build())
        );

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() ->
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build())
        );

        // Tạo tài khoản mẫu thông thường (kiểm tra cả username và email)
        if (!userRepository.existsByUsername("user01") && !userRepository.existsByEmail("user01@gmail.com")) {
            userRepository.save(User.builder()
                .username("user01")
                .email("user01@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .fullName("Lý Gia Hân")
                .images("/images/user.png")
                .role(userRole)
                .enabled(true)
                .build()
            );
            log.info("Đã khởi tạo tài khoản mẫu user01");
        }

        // Tạo tài khoản quản trị viên admin01 (kiểm tra cả username và email)
        if (!userRepository.existsByUsername("admin01") && !userRepository.existsByEmail("admin01@gmail.com")) {
            userRepository.save(User.builder()
                .username("admin01")
                .email("admin01@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .fullName("Quản Trị Viên")
                .images("/images/user.png")
                .role(adminRole)
                .enabled(true)
                .build()
            );
            log.info("Đã khởi tạo tài khoản mẫu admin01");
        }

        // Tạo tài khoản bị vô hiệu hóa (disabled) để phục vụ kiểm thử (kiểm tra cả username và email)
        if (!userRepository.existsByUsername("disabled01") && !userRepository.existsByEmail("disabled01@gmail.com")) {
            userRepository.save(User.builder()
                .username("disabled01")
                .email("disabled01@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .fullName("Người Dùng Bị Khóa")
                .images("/images/user.png")
                .role(userRole)
                .enabled(false)
                .build()
            );
            log.info("Đã khởi tạo tài khoản mẫu disabled01");
        }
    }
}
