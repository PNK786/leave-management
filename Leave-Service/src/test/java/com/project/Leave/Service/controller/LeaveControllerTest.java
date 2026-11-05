package com.project.Leave.Service.controller;

import com.project.Leave.Service.DTO.LeaveBalanceDTO;
import com.project.Leave.Service.DTO.LeaveResponseDTO;
import com.project.Leave.Service.config.SecurityConfig;
import com.project.Leave.Service.entity.LeaveStatus;
import com.project.Leave.Service.entity.LeaveType;
import com.project.Leave.Service.service.LeaveManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;   // Boot 4 package
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeaveController.class)
@Import(SecurityConfig.class)
class LeaveControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean LeaveManagementService service;
    @MockitoBean JwtDecoder jwtDecoder;   // satisfies the resource-server config; not used by jwt() helper

    // ---------- helpers ----------
    private RequestPostProcessor employee(long id) {
        return SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(j -> j.claim("employeeId", String.valueOf(id)))
                .authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
    }

    private RequestPostProcessor manager() {
        return SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(j -> j.claim("employeeId", "99"))
                .authorities(new SimpleGrantedAuthority("ROLE_MANAGER"));
    }

    private LeaveResponseDTO response(Long id, LeaveStatus status) {
        LeaveResponseDTO dto = new LeaveResponseDTO();
        dto.setLeaveId(id);
        dto.setEmployeeId(4L);
        dto.setLeaveType(LeaveType.CASUAL);
        dto.setStartDate(LocalDate.now().plusDays(1));
        dto.setEndDate(LocalDate.now().plusDays(2));
        dto.setStatus(status);
        dto.setReason("Family function");
        return dto;
    }

    private String body(String reason, LocalDate start, LocalDate end, String type) {
        return """
            {"reason":%s,"startDate":"%s","endDate":"%s","leaveType":%s}
            """.formatted(reason == null ? "null" : "\"" + reason + "\"",
                start, end, type == null ? "null" : "\"" + type + "\"");
    }

    private final LocalDate tomorrow = LocalDate.now().plusDays(1);
    private final LocalDate dayAfter = LocalDate.now().plusDays(2);

    // ---------- POST /api/leaves ----------
    @Test
    void applyLeave_valid_returns201_andUsesEmployeeIdFromJwt() throws Exception {
        when(service.applyLeave(any(), eq(4L))).thenReturn(response(1L, LeaveStatus.PENDING));

        mockMvc.perform(post("/api/leaves").with(employee(4)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("Family function", tomorrow, dayAfter, "CASUAL")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.leaveId").value(1))
                .andExpect(jsonPath("$.employeeId").value(4));

        verify(service).applyLeave(any(), eq(4L));
    }

    @Test
    void applyLeave_withoutToken_returns401() throws Exception {
        mockMvc.perform(post("/api/leaves").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("x", tomorrow, dayAfter, "CASUAL")))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(service);
    }

    @Test
    void applyLeave_asManager_returns403() throws Exception {
        mockMvc.perform(post("/api/leaves").with(manager()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("x", tomorrow, dayAfter, "CASUAL")))
                .andExpect(status().isForbidden());
        verifyNoInteractions(service);
    }

    @Test
    void applyLeave_blankReason_returns400() throws Exception {
        mockMvc.perform(post("/api/leaves").with(employee(4)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("", tomorrow, dayAfter, "CASUAL")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.reason").exists());
        verifyNoInteractions(service);
    }

    @Test
    void applyLeave_missingLeaveType_returns400() throws Exception {
        mockMvc.perform(post("/api/leaves").with(employee(4)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("trip", tomorrow, dayAfter, null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.leaveType").exists());
    }

    @Test
    void applyLeave_invalidLeaveType_returns400() throws Exception {
        mockMvc.perform(post("/api/leaves").with(employee(4)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("trip", tomorrow, dayAfter, "HOLIDAY")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void applyLeave_pastStartDate_returns400() throws Exception {
        mockMvc.perform(post("/api/leaves").with(employee(4)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("trip", LocalDate.now().minusDays(3), dayAfter, "SICK")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.startDate").exists());
    }

    @Test
    void applyLeave_endBeforeStart_returns400() throws Exception {
        mockMvc.perform(post("/api/leaves").with(employee(4)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("trip", LocalDate.now().plusDays(5), LocalDate.now().plusDays(2), "CASUAL")))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test
    void applyLeave_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/api/leaves").with(employee(4)).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ bad json"))
                .andExpect(status().isBadRequest());
    }

    // ---------- GET /api/leaves/my ----------
    @Test
    void getMyLeaves_returns200_withList() throws Exception {
        when(service.allLeaveRequests(4L)).thenReturn(
                List.of(response(1L, LeaveStatus.PENDING), response(2L, LeaveStatus.APPROVED)));

        mockMvc.perform(get("/api/leaves/my").with(employee(4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].status").value("APPROVED"));
    }

    @Test
    void getMyLeaves_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/leaves/my")).andExpect(status().isUnauthorized());
    }

    @Test
    void getMyLeaves_asManager_returns403() throws Exception {
        mockMvc.perform(get("/api/leaves/my").with(manager())).andExpect(status().isForbidden());
    }

    // ---------- GET /api/leaves/pending ----------
    @Test
    void pending_asManager_returns200() throws Exception {
        when(service.pendingLeaveRequests(LeaveStatus.PENDING))
                .thenReturn(List.of(response(1L, LeaveStatus.PENDING)));

        mockMvc.perform(get("/api/leaves/pending").with(manager()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void pending_asEmployee_returns403() throws Exception {
        mockMvc.perform(get("/api/leaves/pending").with(employee(4)))
                .andExpect(status().isForbidden());
    }

    @Test
    void pending_emptyList_returns200_emptyArray() throws Exception {
        when(service.pendingLeaveRequests(LeaveStatus.PENDING)).thenReturn(List.of());

        mockMvc.perform(get("/api/leaves/pending").with(manager()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ---------- PUT approve / reject ----------
    @Test
    void approve_asManager_returns200() throws Exception {
        when(service.approveLeave(1L)).thenReturn(response(1L, LeaveStatus.APPROVED));

        mockMvc.perform(put("/api/leaves/1/approve").with(manager()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void approve_asEmployee_returns403() throws Exception {
        mockMvc.perform(put("/api/leaves/1/approve").with(employee(4)).with(csrf()))
                .andExpect(status().isForbidden());
        verifyNoInteractions(service);
    }

    @Test
    void approve_withoutToken_returns401() throws Exception {
        mockMvc.perform(put("/api/leaves/1/approve").with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void approve_nonNumericId_returns400() throws Exception {
        mockMvc.perform(put("/api/leaves/abc/approve").with(manager()).with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reject_asManager_returns200() throws Exception {
        when(service.rejectLeave(1L)).thenReturn(response(1L, LeaveStatus.REJECTED));

        mockMvc.perform(put("/api/leaves/1/reject").with(manager()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void reject_asEmployee_returns403() throws Exception {
        mockMvc.perform(put("/api/leaves/1/reject").with(employee(4)).with(csrf()))
                .andExpect(status().isForbidden());
        verifyNoInteractions(service);
    }

    // ---------- GET /api/leaves/balance ----------
    @Test
    void balance_returns200_forJwtEmployee() throws Exception {
        when(service.getBalance(4L)).thenReturn(new LeaveBalanceDTO(4L, 2026,
                Map.of(LeaveType.CASUAL, 9, LeaveType.SICK, 10, LeaveType.EARNED, 15)));

        mockMvc.perform(get("/api/leaves/balance").with(employee(4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remaining.CASUAL").value(9));
    }

    @Test
    void balance_asManager_returns403() throws Exception {
        mockMvc.perform(get("/api/leaves/balance").with(manager()))
                .andExpect(status().isForbidden());
    }
}