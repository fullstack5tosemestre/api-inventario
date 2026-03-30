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
import com.smartlogix.inventario.model.Warehouse;
import com.smartlogix.inventario.repository.WarehouseRepository;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    void saveWarehouseDelegatesToRepository() {
        Warehouse warehouse = sampleWarehouse(1L);
        when(warehouseRepository.save(warehouse)).thenReturn(warehouse);

        Warehouse result = warehouseService.saveWarehouse(warehouse);

        assertSame(warehouse, result);
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void findAllDelegatesToRepository() {
        List<Warehouse> expected = List.of(sampleWarehouse(1L));
        when(warehouseRepository.findAll()).thenReturn(expected);

        List<Warehouse> result = warehouseService.findAll();

        assertSame(expected, result);
        verify(warehouseRepository).findAll();
    }

    @Test
    void findByIdDelegatesToRepository() {
        Optional<Warehouse> expected = Optional.of(sampleWarehouse(2L));
        when(warehouseRepository.findById(2L)).thenReturn(expected);

        Optional<Warehouse> result = warehouseService.findById(2L);

        assertSame(expected, result);
        verify(warehouseRepository).findById(2L);
    }

    @Test
    void updateWarehouseSavesWhenIdExists() throws Exception {
        Warehouse warehouse = sampleWarehouse(3L);
        when(warehouseRepository.existsById(3L)).thenReturn(true);
        when(warehouseRepository.save(warehouse)).thenReturn(warehouse);

        Warehouse result = warehouseService.updateWarehouse(warehouse);

        assertSame(warehouse, result);
        verify(warehouseRepository).existsById(3L);
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void updateWarehouseThrowsWhenIdDoesNotExist() {
        Warehouse warehouse = sampleWarehouse(4L);
        when(warehouseRepository.existsById(4L)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> warehouseService.updateWarehouse(warehouse));

        assertEquals("4 does not match any existing warehouses.", ex.getMessage());
        verify(warehouseRepository).existsById(4L);
        verify(warehouseRepository, never()).save(warehouse);
    }

    @Test
    void deleteWarehouseDeletesWhenIdExists() throws Exception {
        when(warehouseRepository.existsById(5L)).thenReturn(true);

        warehouseService.deleteWarehouse(5L);

        verify(warehouseRepository).existsById(5L);
        verify(warehouseRepository).deleteById(5L);
    }

    @Test
    void deleteWarehouseThrowsWhenIdDoesNotExist() {
        when(warehouseRepository.existsById(6L)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> warehouseService.deleteWarehouse(6L));

        assertEquals("6 does not match any existing warehouses.", ex.getMessage());
        verify(warehouseRepository).existsById(6L);
        verify(warehouseRepository, never()).deleteById(6L);
    }

    private Warehouse sampleWarehouse(Long id) {
        Branch branch = new Branch(1L, "Central", "Main St");
        return new Warehouse(id, "Main Warehouse", branch);
    }
}
