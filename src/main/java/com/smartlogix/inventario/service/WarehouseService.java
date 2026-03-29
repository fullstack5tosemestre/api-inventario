package com.smartlogix.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartlogix.inventario.model.Warehouse;
import com.smartlogix.inventario.repository.WarehouseRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    // Create
    public Warehouse saveWarehouse(Warehouse w) {
        return warehouseRepository.save(w);
    }

    // Read
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> findById(Long id) {
        return warehouseRepository.findById(id);
    }

    // Update
    public Warehouse updateWarehouse(Warehouse w) throws Exception {

        if (warehouseRepository.existsById(w.getId())) {
            return saveWarehouse(w);
        }

        throw new Exception(w.getId() + " does not match any existing warehouses.");
    }

    // Delete
    public void deleteWarehouse(Long id) throws Exception {
        if (!warehouseRepository.existsById(id)) {
            throw new Exception(id + " does not match any existing warehouses.");
        }

        warehouseRepository.deleteById(id);
    }

}
