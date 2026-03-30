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
import com.smartlogix.inventario.repository.BranchRepository;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    @Test
    void saveBranchDelegatesToRepository() {
        Branch branch = new Branch(1L, "Centro", "Av 1");
        when(branchRepository.save(branch)).thenReturn(branch);

        Branch result = branchService.saveBranch(branch);

        assertSame(branch, result);
        verify(branchRepository).save(branch);
    }

    @Test
    void findAllDelegatesToRepository() {
        List<Branch> expected = List.of(new Branch(1L, "Centro", "Av 1"));
        when(branchRepository.findAll()).thenReturn(expected);

        List<Branch> result = branchService.findAll();

        assertSame(expected, result);
        verify(branchRepository).findAll();
    }

    @Test
    void findByIdDelegatesToRepository() {
        Optional<Branch> expected = Optional.of(new Branch(2L, "Norte", "Av 2"));
        when(branchRepository.findById(2L)).thenReturn(expected);

        Optional<Branch> result = branchService.findById(2L);

        assertSame(expected, result);
        verify(branchRepository).findById(2L);
    }

    @Test
    void updateBranchSavesWhenIdExists() throws Exception {
        Branch branch = new Branch(3L, "Sur", "Av 3");
        when(branchRepository.existsById(3L)).thenReturn(true);
        when(branchRepository.save(branch)).thenReturn(branch);

        Branch result = branchService.updateBranch(branch);

        assertSame(branch, result);
        verify(branchRepository).existsById(3L);
        verify(branchRepository).save(branch);
    }

    @Test
    void updateBranchThrowsWhenIdDoesNotExist() {
        Branch branch = new Branch(4L, "Este", "Av 4");
        when(branchRepository.existsById(4L)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> branchService.updateBranch(branch));

        assertEquals("4 does not match any existing branches.", ex.getMessage());
        verify(branchRepository).existsById(4L);
        verify(branchRepository, never()).save(branch);
    }

    @Test
    void deleteBranchDeletesWhenIdExists() throws Exception {
        when(branchRepository.existsById(5L)).thenReturn(true);

        branchService.deleteBranch(5L);

        verify(branchRepository).existsById(5L);
        verify(branchRepository).deleteById(5L);
    }

    @Test
    void deleteBranchThrowsWhenIdDoesNotExist() {
        when(branchRepository.existsById(6L)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> branchService.deleteBranch(6L));

        assertEquals("6 does not match any existing branches.", ex.getMessage());
        verify(branchRepository).existsById(6L);
        verify(branchRepository, never()).deleteById(6L);
    }
}
