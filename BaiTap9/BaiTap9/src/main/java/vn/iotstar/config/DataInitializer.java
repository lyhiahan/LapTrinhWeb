package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               @Value("${ADMIN_EMAIL:admin@gmail.com}") String adminEmail,
                               @Value("${ADMIN_PASSWORD:123456}") String adminPassword) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

            userRepository.findByUsername("user01").ifPresentOrElse(
                    user -> {
                        user.setFullName("Lý Gia Hân");
                        userRepository.save(user);
                    },
                    () -> {
                        User user = User.builder()
                                .username("user01")
                                .email("user01@gmail.com")
                                .password(passwordEncoder.encode("123456"))
                                .fullName("Lý Gia Hân")
                                .images("/images/user.png")
                                .role(userRole)
                                .enabled(true)
                                .build();
                        userRepository.save(user);
                    }
            );

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email(adminEmail.toLowerCase())
                        .password(passwordEncoder.encode(adminPassword))
                        .fullName("System Administrator")
                        .images("/images/avatar-default.png")
                        .role(adminRole)
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}