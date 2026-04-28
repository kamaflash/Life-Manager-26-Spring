package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.shareddto.dto.products.ProductDTO;
import com.pet.businessdomain.shareddto.dto.products.ProductFilterDTO;
import com.pet.businessdomain.shareddto.dto.products.ProductShopOfferDTO;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    // ========== CRUD BÁSICO ==========
    ProductDTO create(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    ProductDTO getById(Long id);
    ProductDTO getByIdWithEffects(Long id);  // ← NUEVO: Obtener con efectos
    void delete(Long id);
    void deactivate(Long id);
    void deactivateAll();

    // ========== LISTADOS ==========
    List<ProductDTO> getAllActive();
    List<ProductDTO> getAllActiveWithoutEffects();
    List<ProductDTO> getByCategory(ProductCategory category);
    Page<ProductDTO> getFiltered(ProductFilterDTO filters, Pageable pageable);

    // ========== OFERTAS ==========
    List<ProductDTO> getActiveDiscounts();
    ProductShopOfferDTO createOffer(ProductShopOfferDTO offerDTO);
    List<ProductShopOfferDTO> getActiveOffers();

    // ========== RECOMENDACIONES ==========
    List<ProductDTO> getRecommendations(Long characterId, int limit);

    // ========== ADMIN ==========
    void updateStock(Long productId, Integer quantity);
    void applyDiscount(Long productId, BigDecimal discountPercentage, Integer days);
}