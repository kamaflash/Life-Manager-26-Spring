package com.pet.businessdomain.productservice.controller;

import com.pet.businessdomain.productservice.services.ProductService;
import com.pet.businessdomain.shareddto.dto.products.*;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ========== CRUD BÁSICO ==========
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO dto) {
        log.info("POST /api/products - name: {}", dto.getName());
        ProductDTO created = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductDTO>> createProductBatch(@RequestBody List<ProductDTO> dtos) {
        log.info("POST /api/products/batch - size: {}", dtos.size());
        List<ProductDTO> saved = dtos.stream()
                .map(productService::create)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductDTO dto) {
        log.info("PUT /api/products/{}", id);
        ProductDTO updated = productService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Endpoint para obtener producto SIN efectos (usar en listados)
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/products/{} (sin efectos)", id);
        ProductDTO product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    // Endpoint para obtener producto CON efectos (usar en detalle)
    @GetMapping("/{id}/full")
    public ResponseEntity<ProductDTO> getByIdWithEffects(@PathVariable(name = "id") Long id) {
        log.info("GET /api/products/{}/full (con efectos)", id);
        ProductDTO product = productService.getByIdWithEffects(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        log.info("DELETE /api/products/{}", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") Long id) {
        log.info("PUT /api/products/{}/deactivate", id);
        productService.deactivate(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        log.info("DELETE /api/products/all");
        productService.deactivateAll();
        return ResponseEntity.noContent().build();
    }

    // ========== LISTADOS ==========
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllActive() {
        log.info("GET /api/products");
        List<ProductDTO> products = productService.getAllActive();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDTO>> getByCategory(@PathVariable(name = "category") ProductCategory category) {
        log.info("GET /api/products/category/{}", category);
        List<ProductDTO> products = productService.getByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductDTO>> getFiltered(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "category", required = false) ProductCategory category,
            @RequestParam(name = "rarity", required = false) String rarity,
            @RequestParam(name = "slot", required = false) String slot,
            @RequestParam(name = "minLevel", required = false) Integer minLevel,
            @RequestParam(name = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "isConsumable", required = false) Boolean isConsumable,
            @RequestParam(name = "isEventItem", required = false) Boolean isEventItem,
            @RequestParam(name = "season", required = false) String season,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("GET /api/products/filter - category: {}, rarity: {}, page: {}", category, rarity, pageable.getPageNumber());

        ProductFilterDTO filters = ProductFilterDTO.builder()
                .search(search)
                .category(category)
                .rarity(rarity != null ? ProductRarityDTO.valueOf(rarity.toUpperCase()) : null)
                .slot(slot != null ? ProductSlotDTO.valueOf(slot.toUpperCase()) : null)
                .minLevel(minLevel)
                .maxLevel(maxLevel)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .isConsumable(isConsumable)
                .isEventItem(isEventItem)
                .season(season)
                .build();

        Page<ProductDTO> products = productService.getFiltered(filters, pageable);
        return ResponseEntity.ok(products);
    }

    // ========== OFERTAS ==========
    @GetMapping("/discounts")
    public ResponseEntity<List<ProductDTO>> getActiveDiscounts() {
        log.info("GET /api/products/discounts");
        List<ProductDTO> discounts = productService.getActiveDiscounts();
        return ResponseEntity.ok(discounts);
    }

    @PostMapping("/offers")
    public ResponseEntity<ProductShopOfferDTO> createOffer(@RequestBody ProductShopOfferDTO offerDTO) {
        log.info("POST /api/products/offers - productId: {}", offerDTO.getProductId());
        ProductShopOfferDTO created = productService.createOffer(offerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/offers/active")
    public ResponseEntity<List<ProductShopOfferDTO>> getActiveOffers() {
        log.info("GET /api/products/offers/active");
        List<ProductShopOfferDTO> offers = productService.getActiveOffers();
        return ResponseEntity.ok(offers);
    }

    // ========== RECOMENDACIONES ==========
    @GetMapping("/recommendations/{characterId}")
    public ResponseEntity<List<ProductDTO>> getRecommendations(
            @PathVariable(name = "characterId") Long characterId,
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        log.info("GET /api/products/recommendations/{} - limit: {}", characterId, limit);
        List<ProductDTO> recommendations = productService.getRecommendations(characterId, limit);
        return ResponseEntity.ok(recommendations);
    }

    // ========== ADMIN ==========
    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "quantity") Integer quantity) {
        log.info("PUT /api/products/{}/stock - quantity: {}", id, quantity);
        productService.updateStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/discount")
    public ResponseEntity<Void> applyDiscount(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "percentage") BigDecimal discountPercentage,
            @RequestParam(name = "days", defaultValue = "7") Integer days) {
        log.info("POST /api/products/{}/discount - {}% for {} days", id, discountPercentage, days);
        productService.applyDiscount(id, discountPercentage, days);
        return ResponseEntity.ok().build();
    }
}