package com.smartlogix.inventario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartlogix.inventario.model.Branch;
import com.smartlogix.inventario.model.Product;
import com.smartlogix.inventario.model.Warehouse;
import com.smartlogix.inventario.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void saveProductDelegatesToRepository() {
        Product product = sampleProduct(1L);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.saveProduct(product);

        assertSame(product, result);
        verify(productRepository).save(product);
    }

    @Test
    void findAllDelegatesToRepository() {
        List<Product> expected = List.of(sampleProduct(1L));
        when(productRepository.findAll()).thenReturn(expected);

        List<Product> result = productService.findAll();

        assertSame(expected, result);
        verify(productRepository).findAll();
    }

    @Test
    void findByIdDelegatesToRepository() {
        Optional<Product> expected = Optional.of(sampleProduct(2L));
        when(productRepository.findById(2L)).thenReturn(expected);

        Optional<Product> result = productService.findById(2L);

        assertSame(expected, result);
        verify(productRepository).findById(2L);
    }

    @Test
    void updateProductSavesWhenIdExists() throws Exception {
        Product product = sampleProduct(3L);
        when(productRepository.existsById(3L)).thenReturn(true);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProduct(product);

        assertSame(product, result);
        verify(productRepository).existsById(3L);
        verify(productRepository).save(product);
    }

    @Test
    void updateProductThrowsWhenIdDoesNotExist() {
        Product product = sampleProduct(4L);
        when(productRepository.existsById(4L)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> productService.updateProduct(product));

        assertEquals("4 does not match any existing products.", ex.getMessage());
        verify(productRepository).existsById(4L);
        verify(productRepository, never()).save(product);
    }

    @Test
    void deleteProductDeletesWhenIdExists() throws Exception {
        when(productRepository.existsById(5L)).thenReturn(true);

        productService.deleteProduct(5L);

        verify(productRepository).existsById(5L);
        verify(productRepository).deleteById(5L);
    }

    @Test
    void deleteProductThrowsWhenIdDoesNotExist() {
        when(productRepository.existsById(6L)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> productService.deleteProduct(6L));

        assertEquals("6 does not match any existing products.", ex.getMessage());
        verify(productRepository).existsById(6L);
        verify(productRepository, never()).deleteById(6L);
    }

    private Product sampleProduct(Long id) {
        Branch branch = new Branch(1L, "Central", "Main St");
        Warehouse warehouse = new Warehouse(1L, "Main Warehouse", branch);
        return new Product(id, "Notebook", "SKU-1", 20, warehouse);
    }
}
