package com.pet.businessdomain.productservice.controller;

import com.pet.businessdomain.productservice.dto.ProductResponseDTO;
import com.pet.businessdomain.productservice.entities.enumentities.ProductCategory;
import com.pet.businessdomain.productservice.mapper.ProductMapper;
import com.pet.businessdomain.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final ProductMapper productMapper;

    @PostMapping
    public ProductResponseDTO create(@RequestBody ProductResponseDTO dto) {
        return productService.create(dto);
    }
    @PostMapping("/batch")
    public List<ProductResponseDTO> createProductBatch(@RequestBody List<ProductResponseDTO> dtos) {
        List<ProductResponseDTO> saved = new ArrayList<>();
        for (ProductResponseDTO dto : dtos) {
            saved.add(productService.create(dto));
        }
        return saved;
    }
    @GetMapping
    public List<ProductResponseDTO> getAllActive() {
        return productService.getAllActive();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getById(@PathVariable(name = "id") Long id) {
        return productService.getById(id);
    }

    @GetMapping("/category/{category}")
    public List<ProductResponseDTO> getByCategory(@PathVariable(name = "category") ProductCategory category) {
        return productService.getByCategory(category);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable(name = "id") Long id) {
        productService.deactivate(id);
    }
    @DeleteMapping("/all")
    public ResponseEntity<Void> cancelAll() {
        productService.deactivateAll();
        return ResponseEntity.noContent().build();
    }
}
