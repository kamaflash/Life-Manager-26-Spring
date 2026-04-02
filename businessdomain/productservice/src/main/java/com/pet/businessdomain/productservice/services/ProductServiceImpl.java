package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.shareddto.dto.ProductResponseDTO;
import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.productservice.mapper.ProductMapper;
import com.pet.businessdomain.productservice.repository.ProductRepository;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductResponseDTO create(ProductResponseDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Product data is required");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (productRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Product already exists with name: " + dto.getName());
        }

        Product product = productMapper.toEntity(dto);
        product.setActive(true);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    @Override
    public List<ProductResponseDTO> getAllActive() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getByCategory(ProductCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Product category is required");
        }
        return productRepository.findByCategoryAndActiveTrue(category)
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public void deactivate(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }
    @Override
    public void deactivateAll() {
        productRepository.deleteAll();
    }
}
