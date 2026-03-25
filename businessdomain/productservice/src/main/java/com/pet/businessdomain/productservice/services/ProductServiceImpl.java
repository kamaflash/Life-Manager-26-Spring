package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.shareddto.dto.ProductResponseDTO;
import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.productservice.mapper.ProductMapper;
import com.pet.businessdomain.productservice.repository.ProductRepository;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO create(ProductResponseDTO dto) {
        Product product = productMapper.toEntity(dto);
        return productMapper.toDTO(productRepository.save(product));
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
