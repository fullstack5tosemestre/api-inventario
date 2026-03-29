package com.smartlogix.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartlogix.inventario.model.Branch;
import com.smartlogix.inventario.repository.BranchRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    // Create
    public Branch saveBranch(Branch b) {
        return branchRepository.save(b);
    }

    // Read
    public List<Branch> findAll() {
        return branchRepository.findAll();
    }

    public Optional<Branch> findById(Long id) {
        return branchRepository.findById(id);
    }

    // Update
    public Branch updateBranch(Branch b) throws Exception {

        if (branchRepository.existsById(b.getId())) {
            return saveBranch(b);
        }

        throw new Exception(b.getId() + " does not match any existing branches.");
    }

    // Delete
    public void deleteBranch(Long id) throws Exception {
        if (!branchRepository.existsById(id)) {
            throw new Exception(id + " does not match any existing branches.");
        }

        branchRepository.deleteById(id);
    }

}
