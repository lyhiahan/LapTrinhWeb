package vn.iotstar.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProductControllerMvcTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetProductsAsUser() throws Exception {
        mockMvc.perform(get("/products"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "adminuser", roles = {"ADMIN"})
    void testGetProductsAsAdmin() throws Exception {
        mockMvc.perform(get("/products"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetProductsAnonymous() throws Exception {
        mockMvc.perform(get("/products"))
            .andExpect(status().is3xxRedirection());
    }
}
