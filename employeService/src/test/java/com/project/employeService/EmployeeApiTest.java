package com.project.employeService;

import com.project.employeService.entity.Employees;
import com.project.employeService.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeApiTest
{
    @Autowired
    MockMvc mockMvc;
    @Autowired
    EmployeeRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() { repository.deleteAll(); }

    private String body(String name, String email) {
        return """
            {"name":"%s","email":"%s","password":"Secret123"}
            """.formatted(name, email);
    }

    private ResultActions register(String name, String email) throws Exception {
        return mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body(name, email)));
    }

    @Test
    void registerValid_returns201() throws Exception {
        register("Afroz", "afroz@gmail.com").andExpect(status().isCreated());
    }

    @Test void registerDuplicateEmail_returns409() throws Exception {
        register("Afroz", "afroz@gmail.com");
        register("Other", "afroz@gmail.com").andExpect(status().isConflict());
    }

    @Test void registerInvalidEmail_returns400() throws Exception {
        register("Afroz", "not-an-email").andExpect(status().isBadRequest());
    }

    @Test void registerBlankName_returns400() throws Exception {
        register("", "afroz@gmail.com").andExpect(status().isBadRequest());
    }

    @Test void getExisting_returns200() throws Exception {
        register("Afroz", "afroz@gmail.com");
        Long id = repository.findByEmail("afroz@gmail.com").get().getId();

        mockMvc.perform(get("/api/emp/" + id).with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk());
    }

    @Test void getNonexistent_returns404() throws Exception {
        mockMvc.perform(get("/api/employee/9999").with(httpBasic("admin", "admin123")))
                .andExpect(status().isNotFound());
    }

    @Test void getWithoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/employee/1")).andExpect(status().isUnauthorized());
    }

    @Test void passwordIsStoredHashed() throws Exception {
        register("Afroz", "afroz@gmail.com");
        Employees saved = repository.findByEmail("afroz@gmail.com").get();

        assertNotEquals("Secret123", saved.getPassword());
        assertTrue(passwordEncoder.matches("Secret123", saved.getPassword()));
    }
    @Test
    void clientCannotChooseRole() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {"name":"Evil","email":"evil@gmail.com","password":"Secret123","role":"ADMIN"}
                """))
                .andExpect(status().isCreated());

        assertEquals("EMPLOYEE",
                repository.findByEmail("evil@gmail.com").get().getRole());
    }
}
