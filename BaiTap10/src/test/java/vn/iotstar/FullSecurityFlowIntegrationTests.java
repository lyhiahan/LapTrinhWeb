package vn.iotstar;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import vn.iotstar.entities.User;
import vn.iotstar.models.RegisterUserDto;
import vn.iotstar.repositories.UserRepository;
import vn.iotstar.services.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FullSecurityFlowIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        // Dọn dẹp và chuẩn bị người dùng mẫu
        userRepository.findByEmail("admin_test@test.local").ifPresent(userRepository::delete);
        userRepository.findByEmail("normal_test@test.local").ifPresent(userRepository::delete);

        adminUser = new User();
        adminUser.setEmail("admin_test@test.local");
        adminUser.setFullName("Admin Tester");
        adminUser.setPassword(passwordEncoder.encode("123456"));
        adminUser.setRole("ROLE_ADMIN");
        adminUser.setAccountNonLocked(true);
        adminUser.setEnabled(true);
        adminUser = userRepository.save(adminUser);

        normalUser = new User();
        normalUser.setEmail("normal_test@test.local");
        normalUser.setFullName("Normal Tester");
        normalUser.setPassword(passwordEncoder.encode("123456"));
        normalUser.setRole("ROLE_USER");
        normalUser.setAccountNonLocked(true);
        normalUser.setEnabled(true);
        normalUser = userRepository.save(normalUser);
    }

    @Test
    void testRegisterWithAdminKeywordDoesNotGrantAdminRole() throws Exception {
        userRepository.findByEmail("student-admin@gmail.com").ifPresent(userRepository::delete);

        RegisterUserDto dto = new RegisterUserDto("student-admin@gmail.com", "pass123", "Sinh Vien Admin");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ROLE_USER"));

        User saved = userRepository.findByEmail("student-admin@gmail.com").orElseThrow();
        Assertions.assertEquals("ROLE_USER", saved.getRole(), "Không được phép tự nâng quyền thành ROLE_ADMIN qua email!");
    }

    @Test
    void testRegularUserCannotLockAccountReturns403() throws Exception {
        String normalToken = jwtService.generateToken(normalUser);

        mockMvc.perform(patch("/users/" + adminUser.getId() + "/lock")
                        .header("Authorization", "Bearer " + normalToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.description").value("Không được phép truy cập vào tài nguyên"));
    }

    @Test
    void testAdminCanLockAndUnlockOtherUser() throws Exception {
        String adminToken = jwtService.generateToken(adminUser);

        // 1. Admin khóa tài khoản người khác -> Thành công 200
        mockMvc.perform(patch("/users/" + normalUser.getId() + "/lock")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNonLocked").value(false));

        User lockedInDb = userRepository.findById(normalUser.getId()).orElseThrow();
        Assertions.assertFalse(lockedInDb.isAccountNonLocked());

        // 2. Admin mở khóa -> Thành công 200
        mockMvc.perform(patch("/users/" + normalUser.getId() + "/unlock")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNonLocked").value(true));
    }

    @Test
    void testAdminCannotLockSelfReturns403() throws Exception {
        String adminToken = jwtService.generateToken(adminUser);

        mockMvc.perform(patch("/users/" + adminUser.getId() + "/lock")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.detail").value("Bạn không thể tự khóa tài khoản của chính mình!"));
    }

    @Test
    void testOldJwtOfLockedUserReturns403OnFilter() throws Exception {
        // Sinh JWT hợp lệ cho normalUser khi tài khoản còn hoạt động
        String oldToken = jwtService.generateToken(normalUser);

        // Giả lập tài khoản bị khóa trong database sau khi đã cấp token
        normalUser.setAccountNonLocked(false);
        userRepository.save(normalUser);

        // Dùng token cũ gọi /users/me -> Filter phải chặn ngay và ném 403
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + oldToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.description").value("Tài khoản bị khóa"));
    }

    @Test
    void testGenuineExpiredTokenThrows401OnFilter() throws Exception {
        // Token có chữ ký thật 100% bằng secret key nhưng exp trong quá khứ
        String expiredToken = jwtService.generateExpiredToken("normal_test@test.local");

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.description").value("JWT đã hết hạn"));
    }

    @Test
    void testDatabaseRoleColumnState() {
        User user = new User();
        user.setEmail("role_check@test.local");
        user.setFullName("Role Checker");
        user.setPassword(passwordEncoder.encode("123456"));
        user = userRepository.save(user);

        Assertions.assertNotNull(user.getRole());
        Assertions.assertEquals("ROLE_USER", user.getRole());
        userRepository.delete(user);
    }
}
