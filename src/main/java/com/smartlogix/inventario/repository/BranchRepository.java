package com.smartlogix.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlogix.inventario.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

}
