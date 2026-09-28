package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@SpringBootApplication
public class BaiTap9ViDu2Application {

    public static void main(String[] args) {
        SpringApplication.run(BaiTap9ViDu2Application.class, args);
    }

    @Bean
    CommandLineRunner init(
        RoleRepository roleRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role userRole = roleRepository
                .findByName("ROLE_USER")
                .orElseGet(() ->
                    roleRepository.save(
                        Role.builder()
                            .name("ROLE_USER")
                            .build()
                    )
                );

            userRepository.findByUsername("user01").ifPresentOrElse(
                user -> {
                    user.setFullName("Lý Gia Hân");
                    user.setImages("/images/user.png");
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
        };
    }
}
