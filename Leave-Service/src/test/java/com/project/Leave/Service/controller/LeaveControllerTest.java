package com.project.Leave.Service.controller;

import com.project.Leave.Service.DTO.LeaveRequestDTO;
import com.project.Leave.Service.DTO.LeaveResponseDTO;
import com.project.Leave.Service.entity.LeaveType;
import com.project.Leave.Service.service.LeaveManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaveController.class)
@WithMockUser
public class LeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LeaveManagementService leaveManagementService;

    private LeaveRequestDTO validRequest() {
        LeaveRequestDTO request = new LeaveRequestDTO();
        request.setEmpId(8L);
        request.setLeaveType(LeaveType.CASUAL);
        request.setReason("Paternal Leave");
        request.setStartDate(LocalDate.now().plusDays(7));
        request.setEndDate(LocalDate.now().plusDays(17));
        return request;
    }

    @Test
    void applyLeave_validRequest_return201() throws Exception {
        LeaveResponseDTO response = new LeaveResponseDTO();
        response.setLeaveId(5L);
        response.setLeaveType(LeaveType.CASUAL);
        response.setReason("Paternal Leave");
        response.setDuration(11L);

        when(leaveManagementService.applyLeave(any(LeaveRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/leaves")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.leaveId").value(5))
                .andExpect(jsonPath("$.leaveType").value("CASUAL"));

        verify(leaveManagementService).applyLeave(any(LeaveRequestDTO.class));
    }

    @Test
    void applyLeave_emptyBody_returns400() throws Exception {
        mockMvc.perform(post("/api/leaves")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(leaveManagementService, never()).applyLeave(any());
    }

    @Test
    void applyLeave_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/api/leaves")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void applyLeave_wrongContentType_returns415() throws Exception {
        mockMvc.perform(post("/api/leaves")
                        .with(csrf())
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("hello"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void applyLeave_getMethod_returns405() throws Exception {
        mockMvc.perform(get("/api/leaves"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void applyLeave_withoutCsrf_returns403() throws Exception {
        mockMvc.perform(post("/api/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isForbidden());
    }

}
