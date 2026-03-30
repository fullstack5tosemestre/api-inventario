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
import com.smartlogix.inventario.model.Warehouse;
import com.smartlogix.inventario.service.WarehouseService;

@ExtendWith(MockitoExtension.class)
class WarehouseControllerTest {

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private WarehouseController warehouseController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(warehouseController).build();
    }

    @Test
    void getWarehousesReturnsOkWhenListIsNotEmpty() throws Exception {
        when(warehouseService.findAll()).thenReturn(List.of(sampleWarehouse(1L, "A")));

        mockMvc.perform(get("/api/v1/warehouses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("A"));
    }

    @Test
    void getWarehousesReturnsNoContentWhenListIsEmpty() throws Exception {
        when(warehouseService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/warehouses"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findWarehouseReturnsOkWhenFound() throws Exception {
        when(warehouseService.findById(3L)).thenReturn(Optional.of(sampleWarehouse(3L, "B")));

        mockMvc.perform(get("/api/v1/warehouses/{id}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L));
    }

    @Test
    void findWarehouseReturnsNotFoundWhenMissing() throws Exception {
        when(warehouseService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/warehouses/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWarehouseReturnsCreated() throws Exception {
        Warehouse request = sampleWarehouse(null, "B");
        Warehouse saved = sampleWarehouse(4L, "B");
        when(warehouseService.saveWarehouse(any(Warehouse.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4L));
    }

    @Test
    void updateWarehouseReturnsOkAndUsesPathId() throws Exception {
        Warehouse request = sampleWarehouse(700L, "C");
        Warehouse updated = sampleWarehouse(12L, "C");
        when(warehouseService.updateWarehouse(any(Warehouse.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/warehouses/{id}", 12L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(12L));

        ArgumentCaptor<Warehouse> captor = ArgumentCaptor.forClass(Warehouse.class);
        verify(warehouseService).updateWarehouse(captor.capture());
        org.junit.jupiter.api.Assertions.assertEquals(12L, captor.getValue().getId());
    }

    @Test
    void updateWarehouseReturnsNotFoundWhenServiceThrows() throws Exception {
        Warehouse request = sampleWarehouse(null, "C");
        when(warehouseService.updateWarehouse(any(Warehouse.class))).thenThrow(new Exception("missing"));

        mockMvc.perform(put("/api/v1/warehouses/{id}", 12L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWarehouseReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/warehouses/{id}", 12L))
                .andExpect(status().isNoContent());

        verify(warehouseService).deleteWarehouse(12L);
    }

    @Test
    void deleteWarehouseReturnsNotFoundWhenServiceThrows() throws Exception {
        doThrow(new Exception("missing")).when(warehouseService).deleteWarehouse(12L);

        mockMvc.perform(delete("/api/v1/warehouses/{id}", 12L))
                .andExpect(status().isNotFound());
    }

    private Warehouse sampleWarehouse(Long id, String name) {
        Branch branch = new Branch(1L, "Central", "Main St");
        return new Warehouse(id, name, branch);
    }
}
