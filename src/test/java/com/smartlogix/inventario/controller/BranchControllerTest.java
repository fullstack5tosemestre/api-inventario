package com.smartlogix.inventario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.inventario.model.Branch;
import com.smartlogix.inventario.service.BranchService;

@ExtendWith(MockitoExtension.class)
class BranchControllerTest {

    @Mock
    private BranchService branchService;

    @InjectMocks
    private BranchController branchController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(branchController).build();
    }

    @Test
    void getBranchesReturnsOkWhenListIsNotEmpty() throws Exception {
        when(branchService.findAll()).thenReturn(List.of(new Branch(1L, "Centro", "Av 1")));

        mockMvc.perform(get("/api/v1/branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Centro"));
    }

    @Test
    void getBranchesReturnsNoContentWhenListIsEmpty() throws Exception {
        when(branchService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/branches"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findBranchReturnsOkWhenFound() throws Exception {
        when(branchService.findById(3L)).thenReturn(Optional.of(new Branch(3L, "Norte", "Av 3")));

        mockMvc.perform(get("/api/v1/branches/{id}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L));
    }

    @Test
    void findBranchReturnsNotFoundWhenMissing() throws Exception {
        when(branchService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/branches/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBranchReturnsCreated() throws Exception {
        Branch request = new Branch(null, "Sur", "Av 4");
        Branch saved = new Branch(4L, "Sur", "Av 4");
        when(branchService.saveBranch(any(Branch.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4L));
    }

    @Test
    void updateBranchReturnsOkAndUsesPathId() throws Exception {
        Branch request = new Branch(900L, "Sur", "Av 4");
        Branch updated = new Branch(11L, "Sur", "Av 4");
        when(branchService.updateBranch(any(Branch.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/branches/{id}", 11L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11L));

        ArgumentCaptor<Branch> captor = ArgumentCaptor.forClass(Branch.class);
        verify(branchService).updateBranch(captor.capture());
        org.junit.jupiter.api.Assertions.assertEquals(11L, captor.getValue().getId());
    }

    @Test
    void updateBranchReturnsNotFoundWhenServiceThrows() throws Exception {
        Branch request = new Branch(null, "Sur", "Av 4");
        when(branchService.updateBranch(any(Branch.class))).thenThrow(new Exception("missing"));

        mockMvc.perform(put("/api/v1/branches/{id}", 11L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBranchReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/branches/{id}", 11L))
                .andExpect(status().isNoContent());

        verify(branchService).deleteBranch(11L);
    }

    @Test
    void deleteBranchReturnsNotFoundWhenServiceThrows() throws Exception {
        doThrow(new Exception("missing")).when(branchService).deleteBranch(11L);

        mockMvc.perform(delete("/api/v1/branches/{id}", 11L))
                .andExpect(status().isNotFound());
    }
}
