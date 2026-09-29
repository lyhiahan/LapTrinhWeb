package vn.iotstar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.security.CustomUserDetailsService;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomLoginIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldAllowAccessToLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void shouldLoginSuccessfullyWithUsername() throws Exception {
        mockMvc.perform(formLogin("/login").user("user01").password("123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("user01"));
    }

    @Test
    void shouldLoginSuccessfullyWithEmail() throws Exception {
        mockMvc.perform(formLogin("/login").user("user01@gmail.com").password("123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("user01"));
    }

    @Test
    void shouldFailLoginWithInvalidCredentials() throws Exception {
        mockMvc.perform(formLogin("/login").user("user01").password("wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"))
                .andExpect(unauthenticated());
    }

    @Test
    void shouldFailLoginWhenAccountIsDisabled() throws Exception {
        // Tài khoản disabled01 có enabled=false
        mockMvc.perform(formLogin("/login").user("disabled01").password("123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"))
                .andExpect(unauthenticated());
    }

    @Test
    void shouldLogoutSuccessfully() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout=true"))
                .andExpect(unauthenticated());
    }

    @Test
    void shouldRedirectAnonymousUserAccessingAdmin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(username = "user01", roles = {"USER"})
    void shouldForbidUserRoleAccessingAdmin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin01", roles = {"ADMIN"})
    void shouldAllowAdminRoleAccessingAdmin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Chào mừng Admin")));
    }

    @Test
    void shouldAllowAccessToH2ConsoleWithoutRedirectingToLogin() throws Exception {
        // H2 console được permitAll, không bị Security chuyển hướng về /login (302)
        // và được cấu hình X-Frame-Options: SAMEORIGIN
        mockMvc.perform(get("/h2-console"))
                .andExpect(status().is(not(302)))
                .andExpect(header().string("X-Frame-Options", "SAMEORIGIN"));
    }

    @Test
    void shouldIgnoreCsrfForH2Console() throws Exception {
        // Gửi POST tới /h2-console mà không có CSRF token, không bị HTTP 403 Forbidden do CSRF
        mockMvc.perform(post("/h2-console"))
                .andExpect(status().is(not(403)));
    }

    @Test
    void shouldHandleUsernameAndEmailConflictGracefully() {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();

        // Giả sử User 1 có username trùng email của User 2
        String sharedIdentity = "shared_conflict@example.com";

        User userWithUsername = userRepository.save(User.builder()
                .username(sharedIdentity)
                .email("other_user1@example.com")
                .password(passwordEncoder.encode("123456"))
                .fullName("Người Dùng Trùng Username")
                .role(userRole)
                .enabled(true)
                .build());

        User userWithEmail = userRepository.save(User.builder()
                .username("unique_user2")
                .email(sharedIdentity)
                .password(passwordEncoder.encode("123456"))
                .fullName("Người Dùng Trùng Email")
                .role(userRole)
                .enabled(true)
                .build());

        try {
            // Khi đăng nhập với sharedIdentity, ưu tiên tìm theo username trước mà không văng ngoại lệ NonUniqueResultException
            UserDetails loaded = customUserDetailsService.loadUserByUsername(sharedIdentity);
            Assertions.assertNotNull(loaded);
            Assertions.assertEquals(sharedIdentity, loaded.getUsername());
        } finally {
            // Dọn dẹp dữ liệu kiểm thử
            userRepository.delete(userWithEmail);
            userRepository.delete(userWithUsername);
        }
    }

    @Test
    @WithUserDetails(value = "user01", userDetailsServiceBeanName = "customUserDetailsService")
    void shouldRenderHomePageWithAuthenticatedPrincipal() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Lý Gia Hân")))
                .andExpect(content().string(containsString("user01")))
                .andExpect(content().string(containsString("user01@gmail.com")))
                .andExpect(content().string(containsString("ROLE_USER")));
    }
}
