package com.smartlogix.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlogix.inventario.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

}
