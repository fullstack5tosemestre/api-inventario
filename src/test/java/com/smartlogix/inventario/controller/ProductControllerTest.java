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
import com.smartlogix.inventario.model.Product;
import com.smartlogix.inventario.model.Warehouse;
import com.smartlogix.inventario.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getProductsReturnsOkWhenListIsNotEmpty() throws Exception {
        Product product = sampleProduct(1L, "Notebook", "SKU-1", 15);
        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Notebook"));
    }

    @Test
    void getProductsReturnsNoContentWhenListIsEmpty() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findProductReturnsOkWhenFound() throws Exception {
        Product product = sampleProduct(5L, "Mouse", "SKU-5", 30);
        when(productService.findById(5L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/products/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.sku").value("SKU-5"));
    }

    @Test
    void findProductReturnsNotFoundWhenMissing() throws Exception {
        when(productService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProductReturnsCreated() throws Exception {
        Product request = sampleProduct(null, "Keyboard", "SKU-2", 8);
        Product saved = sampleProduct(2L, "Keyboard", "SKU-2", 8);
        when(productService.saveProduct(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Keyboard"));
    }

    @Test
    void updateProductReturnsOkAndUsesPathId() throws Exception {
        Product request = sampleProduct(777L, "Desk", "SKU-3", 4);
        Product updated = sampleProduct(10L, "Desk", "SKU-3", 4);
        when(productService.updateProduct(any(Product.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/products/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productService).updateProduct(captor.capture());
        Product forwarded = captor.getValue();
        org.junit.jupiter.api.Assertions.assertEquals(10L, forwarded.getId());
    }

    @Test
    void updateProductReturnsNotFoundWhenServiceThrows() throws Exception {
        Product request = sampleProduct(null, "Desk", "SKU-3", 4);
        when(productService.updateProduct(any(Product.class))).thenThrow(new Exception("missing"));

        mockMvc.perform(put("/api/v1/products/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProductReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/products/{id}", 7L))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(7L);
    }

    @Test
    void deleteProductReturnsNotFoundWhenServiceThrows() throws Exception {
        doThrow(new Exception("missing")).when(productService).deleteProduct(7L);

        mockMvc.perform(delete("/api/v1/products/{id}", 7L))
                .andExpect(status().isNotFound());
    }

    private Product sampleProduct(Long id, String name, String sku, int stock) {
        Branch branch = new Branch(1L, "Central", "Main St");
        Warehouse warehouse = new Warehouse(1L, "Main Warehouse", branch);
        return new Product(id, name, sku, stock, warehouse);
    }
}
