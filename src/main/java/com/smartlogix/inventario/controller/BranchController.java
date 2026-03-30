package com.smartlogix.inventario.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartlogix.inventario.model.Branch;
import com.smartlogix.inventario.service.BranchService;

@RequestMapping("api/v1/branches")
@RestController
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping
    public ResponseEntity<List<Branch>> getBranches() {
        List<Branch> branches = branchService.findAll();

        if (!branches.isEmpty()) {
            return new ResponseEntity<>(branches, HttpStatus.OK);
        }

        return new ResponseEntity<>(branches, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> findBranch(@PathVariable Long id) {
        Optional<Branch> optionalBranch = branchService.findById(id);

        if (optionalBranch.isPresent()) {
            Branch branch = optionalBranch.get();
            return new ResponseEntity<>(branch, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        Branch savedBranch = branchService.saveBranch(branch);
        return new ResponseEntity<>(savedBranch, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch branch) {
        try {
            branch.setId(id);
            Branch updatedBranch = branchService.updateBranch(branch);
            return new ResponseEntity<>(updatedBranch, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        try {
            branchService.deleteBranch(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
