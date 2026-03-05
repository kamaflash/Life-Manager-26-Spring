package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.productservice.dto.ProductResponseDTO;
import com.pet.businessdomain.productservice.entities.enumentities.ProductCategory;

import java.util.List;

public interface ProductService {

    ProductResponseDTO create(ProductResponseDTO dto);

    List<ProductResponseDTO> getAllActive();

    List<ProductResponseDTO> getByCategory(ProductCategory category);

    ProductResponseDTO getById(Long id);

    void deactivate(Long id);

    void deactivateAll();
}
