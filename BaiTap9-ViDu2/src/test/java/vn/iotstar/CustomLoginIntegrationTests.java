package vn.iotstar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomLoginIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

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
    void shouldLogoutSuccessfully() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout=true"))
                .andExpect(unauthenticated());
    }
}
