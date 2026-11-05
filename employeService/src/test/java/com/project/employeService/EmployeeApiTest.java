package com.project.employeService;


import com.project.employeService.DTO.EmployeeResponseDTO;
import com.project.employeService.Exception.ResourceNotFoundException;
import com.project.employeService.config.SecurityConfig;
import com.project.employeService.controller.EmployeeController;
import com.project.employeService.entity.Role;
import com.project.employeService.service.EmployeeService;
import com.project.employeService.service.EmployeeUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(SecurityConfig.class)
class EmployeeControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean EmployeeService employeeService;
    @MockitoBean UserDetailsService userDetailsService;
    @MockitoBean
    EmployeeUserDetailsService employeeUserDetailsService;
    // satisfies security config; not used by user() helper

    private EmployeeResponseDTO response(Long id, String email, String role) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(id);
        dto.setName("Afroz");
        dto.setEmail(email);
        dto.setRole(Role.EMPLOYEE);
        return dto;
    }

    private String registerBody(String name, String email, String password) {
        return """
        {"name":%s,"email":%s,"password":%s}
        """.formatted(q(name), q(email), q(password));
    }

    private String q(String v) {
        return v == null ? "null" : "\"" + v + "\"";
    }

    // ---------- POST /api/employee (public registration) ----------
    @Test
    void register_valid_returns201_withoutAuth() throws Exception {
        when(employeeService.createEmployee(any()))
                .thenReturn(response(1L, "afroz@gmail.com", "EMPLOYEE"));

        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody("Afroz", "afroz@gmail.com", "Secret123")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.role").value("EMPLOYEE"))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(employeeService).createEmployee(any());
    }

    @Test
    void register_blankName_returns400() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody("", "afroz@gmail.com", "Secret123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
        verifyNoInteractions(employeeService);
    }

    @Test
    void register_invalidEmail_returns400() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody("Afroz", "not-an-email", "Secret123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists());
        verifyNoInteractions(employeeService);
    }

    @Test
    void register_shortPassword_returns400() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody("Afroz", "afroz@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    void register_missingFields_returns400_withAllErrors() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    void register_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ bad json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_wrongContentType_returns415() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("hello"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void register_clientSendsRole_isIgnored_serviceStillCalled() throws Exception {
        when(employeeService.createEmployee(any()))
                .thenReturn(response(1L, "evil@gmail.com", "EMPLOYEE"));

        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name":"Evil","email":"evil@gmail.com","password":"Secret123","role":"ADMIN"}
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("EMPLOYEE"));
    }

    // ---------- GET /api/emp/{id} ----------
    @Test
    void getById_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/emp/1")).andExpect(status().isUnauthorized());
        verifyNoInteractions(employeeService);
    }

    @Test
    void getById_authenticated_returns200() throws Exception {
        when(employeeService.getEmpById(eq(1L), any()))
                .thenReturn(response(1L, "afroz@gmail.com", "EMPLOYEE"));

        mockMvc.perform(get("/api/emp/1").with(user("afroz@gmail.com").roles("EMPLOYEE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("afroz@gmail.com"));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        when(employeeService.getEmpById(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("Employee not found with id 99"));

        mockMvc.perform(get("/api/emp/99").with(user("admin@company.com").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with id 99"));
    }

    @Test
    void getById_otherEmployeesRecord_returns403() throws Exception {
        when(employeeService.getEmpById(eq(2L), any()))
                .thenThrow(new AccessDeniedException("You are not allowed to view this employee"));

        mockMvc.perform(get("/api/emp/2").with(user("afroz@gmail.com").roles("EMPLOYEE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getById_nonNumericId_returns400() throws Exception {
        mockMvc.perform(get("/api/emp/abc").with(user("afroz@gmail.com").roles("EMPLOYEE")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_passesAuthenticationToService() throws Exception {
        when(employeeService.getEmpById(eq(1L), any()))
                .thenReturn(response(1L, "afroz@gmail.com", "EMPLOYEE"));

        mockMvc.perform(get("/api/emp/1").with(user("afroz@gmail.com").roles("EMPLOYEE")))
                .andExpect(status().isOk());

        verify(employeeService).getEmpById(eq(1L), argThat(a -> a.getName().equals("afroz@gmail.com")));
    }

    // ---------- GET /api/me ----------
    @Test
    void me_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void me_returnsLoggedInEmployee_usingEmailFromAuthentication() throws Exception {
        when(employeeService.getByEmail("afroz@gmail.com"))
                .thenReturn(response(1L, "afroz@gmail.com", "EMPLOYEE"));

        mockMvc.perform(get("/api/me").with(user("afroz@gmail.com").roles("EMPLOYEE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("afroz@gmail.com"))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(employeeService).getByEmail("afroz@gmail.com");
    }

    @Test
    void me_employeeNotInDb_returns404() throws Exception {
        when(employeeService.getByEmail("ghost@gmail.com"))
                .thenThrow(new ResourceNotFoundException("Employee not found: ghost@gmail.com"));

        mockMvc.perform(get("/api/me").with(user("ghost@gmail.com").roles("EMPLOYEE")))
                .andExpect(status().isNotFound());
    }
}