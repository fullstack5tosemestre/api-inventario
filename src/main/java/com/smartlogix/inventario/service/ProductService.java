package com.smartlogix.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartlogix.inventario.model.Product;
import com.smartlogix.inventario.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Create
    public Product saveProduct(Product p) {
        return productRepository.save(p);
    }

    // Read
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    // Update
    public Product updateProduct(Product p) throws Exception {

        if (productRepository.existsById(p.getId())) {
            return saveProduct(p);
        }

        throw new Exception(p.getId() + " does not match any existing products.");
    }

    // Delete
    public void deleteProduct(Long id) throws Exception {
        if (!productRepository.existsById(id)) {
            throw new Exception(id + " does not match any existing products.");
        }

        productRepository.deleteById(id);
    }

}
