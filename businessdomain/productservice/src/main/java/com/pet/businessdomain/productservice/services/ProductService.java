package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.shareddto.dto.ProductResponseDTO;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;

import java.util.List;

public interface ProductService {

    ProductResponseDTO create(ProductResponseDTO dto);

    List<ProductResponseDTO> getAllActive();

    List<ProductResponseDTO> getByCategory(ProductCategory category);

    ProductResponseDTO getById(Long id);

    void deactivate(Long id);

    void deactivateAll();
}
