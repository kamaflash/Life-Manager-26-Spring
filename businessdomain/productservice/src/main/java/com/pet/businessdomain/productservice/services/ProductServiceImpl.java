package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.productservice.entities.ProductInstantEffect;
import com.pet.businessdomain.productservice.entities.ProductPassiveEffect;
import com.pet.businessdomain.productservice.entities.ProductShopOffer;
import com.pet.businessdomain.productservice.mapper.ProductMapper;
import com.pet.businessdomain.productservice.mapper.ProductShopOfferMapper;
import com.pet.businessdomain.productservice.repository.ProductInstantEffectRepository;
import com.pet.businessdomain.productservice.repository.ProductPassiveEffectRepository;
import com.pet.businessdomain.productservice.repository.ProductRepository;
import com.pet.businessdomain.productservice.repository.ProductShopOfferRepository;
import com.pet.businessdomain.shareddto.dto.products.*;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import com.pet.businessdomain.shareddto.enumentities.products.EffectType;
import com.pet.businessdomain.shareddto.enumentities.products.ProductRarity;
import com.pet.businessdomain.shareddto.enumentities.products.ProductSlot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductShopOfferRepository shopOfferRepository;
    private final ProductMapper productMapper;
    private final ProductShopOfferMapper shopOfferMapper;
    private final ProductInstantEffectRepository instantEffectRepository;
    private final ProductPassiveEffectRepository passiveEffectRepository;

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        log.info("Creating product: {}", dto.getName());

        if (productRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Product already exists with name: " + dto.getName());
        }

        Product product = productMapper.toEntity(dto);
        product.setActive(true);
        product.setCreatedAt(LocalDate.now());

        Product saved = productRepository.save(product);
        return convertToDtoWithoutEffects(saved);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        log.info("Updating product: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productMapper.updateEntity(dto, product);
        product.setUpdatedAt(LocalDate.now());

        Product saved = productRepository.save(product);
        return convertToDtoWithoutEffects(saved);
    }

    @Override
    public ProductDTO getById(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .map(this::convertToDtoWithoutEffects)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    @Transactional
    public ProductDTO getByIdWithEffects(Long id) {
        log.info("Getting product with effects: {}", id);

        Product product = productRepository.findByIdWithEffects(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Crear DTO manualmente sin usar convertToDtoWithEffects para evitar recursión
        return buildProductDTOWithEffects(product);
    }

    // Método alternativo para construir DTO sin recursión
    private ProductDTO buildProductDTOWithEffects(Product product) {
        if (product == null) return null;

        // Efectos instantáneos
        List<ProductInstantEffectDTO> instantEffectsDTO = new ArrayList<>();
        if (product.getInstantEffects() != null) {
            for (ProductInstantEffect effect : product.getInstantEffects()) {
                instantEffectsDTO.add(ProductInstantEffectDTO.builder()
                        .id(effect.getId())
                        .type(EffectTypeDTO.valueOf(effect.getType().name()))
                        .amount(effect.getAmount())
                        .isPercentage(effect.getIsPercentage())
                        .description(effect.getDescription())
                        .formattedDescription(formatInstantEffect(effect))
                        .build());
            }
        }

        // Efectos pasivos
        List<ProductPassiveEffectDTO> passiveEffectsDTO = new ArrayList<>();
        if (product.getPassiveEffects() != null) {
            for (ProductPassiveEffect effect : product.getPassiveEffects()) {
                passiveEffectsDTO.add(ProductPassiveEffectDTO.builder()
                        .id(effect.getId())
                        .stat(effect.getStat())
                        .value(effect.getValue())
                        .isPercentage(effect.getIsPercentage())
                        .trigger(TriggerConditionDTO.valueOf(effect.getTrigger().name()))
                        .description(effect.getDescription())
                        .formattedDescription(formatPassiveEffect(effect))
                        .build());
            }
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .img(product.getImg())
                .icon(product.getIcon())
                .pa(product.getPa())
                .category(product.getCategory())
                .usageType(product.getUsageType())
                .price(product.getPrice())
                .finalPrice(calculateFinalPrice(product))
                .rarity(mapRarityToDTO(product.getRarity()))
                .requiredLevel(product.getRequiredLevel())
                .slot(mapSlotToDTO(product.getSlot()))
                .isConsumable(product.getIsConsumable())
                .stackable(product.getStackable())
                .cooldownHours(product.getCooldownHours())
                .durability(product.getDurability())
                .active(product.getActive())
                .sellable(product.getSellable())
                .instantEffects(instantEffectsDTO)
                .passiveEffects(passiveEffectsDTO)
                .averageRating(calculateAverageRating(product))
                .build();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting product: {}", id);
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        log.info("Deactivating product: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deactivateAll() {
        log.info("Deactivating all products");
        List<Product> products = productRepository.findByActiveTrue();
        for (Product product : products) {
            product.setActive(false);
        }
        productRepository.saveAll(products);
    }

    @Override
    public List<ProductDTO> getAllActive() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::convertToDtoWithoutEffects)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getAllActiveWithoutEffects() {
        return getAllActive();
    }

    @Override
    public List<ProductDTO> getByCategory(ProductCategory category) {
        return productRepository.findByCategoryAndActiveTrue(category)
                .stream()
                .map(this::convertToDtoWithoutEffects)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> getFiltered(ProductFilterDTO filters, Pageable pageable) {
        ProductRarity rarity = filters.getRarity() != null ?
                ProductRarity.valueOf(filters.getRarity().name()) : null;

        ProductSlot slot = filters.getSlot() != null ?
                ProductSlot.valueOf(filters.getSlot().name()) : null;

        return productRepository.findAllWithFilters(
                filters.getSearch(),
                filters.getCategory(),
                rarity,
                slot,
                filters.getMinLevel(),
                filters.getMaxLevel(),
                filters.getMinPrice(),
                filters.getMaxPrice(),
                filters.getIsConsumable(),
                filters.getIsEventItem(),
                filters.getSeason(),
                pageable
        ).map(this::convertToDtoWithoutEffects);
    }

    @Override
    public List<ProductDTO> getActiveDiscounts() {
        return productRepository.findActiveDiscounts()
                .stream()
                .map(this::convertToDtoWithoutEffects)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductShopOfferDTO createOffer(ProductShopOfferDTO offerDTO) {
        log.info("Creating shop offer for product: {}", offerDTO.getProductId());

        ProductShopOffer offer = shopOfferMapper.toEntity(offerDTO);
        offer.setActive(true);
        offer.setOriginalPrice(productRepository.findById(offerDTO.getProductId())
                .map(Product::getPrice)
                .orElse(BigDecimal.ZERO));

        ProductShopOffer saved = shopOfferRepository.save(offer);
        return shopOfferMapper.toDto(saved);
    }

    @Override
    public List<ProductShopOfferDTO> getActiveOffers() {
        return shopOfferRepository.findActiveOffers(LocalDateTime.now())
                .stream()
                .map(shopOfferMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getRecommendations(Long characterId, int limit) {
        return productRepository.findRecommendedProducts(characterId, limit)
                .stream()
                .map(this::convertToDtoWithoutEffects)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        log.info("Updating stock for product {}: +{}", productId, quantity);
    }

    @Override
    @Transactional
    public void applyDiscount(Long productId, BigDecimal discountPercentage, Integer days) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        BigDecimal discountPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(100).subtract(discountPercentage))
                .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);

        product.setDiscountPrice(discountPrice);
        product.setDiscountStartDate(LocalDate.now());
        product.setDiscountEndDate(LocalDate.now().plusDays(days));

        productRepository.save(product);
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Convierte Product a ProductDTO SIN efectos (para listados)
     */
    private ProductDTO convertToDtoWithoutEffects(Product product) {
        if (product == null) return null;

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .img(product.getImg())
                .icon(product.getIcon())
                .pa(product.getPa())
                .category(product.getCategory())
                .usageType(product.getUsageType())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .discountStartDate(product.getDiscountStartDate())
                .discountEndDate(product.getDiscountEndDate())
                .finalPrice(calculateFinalPrice(product))
                .rarity(mapRarityToDTO(product.getRarity()))
                .requiredLevel(product.getRequiredLevel())
                .requiredXp(product.getRequiredXp())
                .slot(mapSlotToDTO(product.getSlot()))
                .isConsumable(product.getIsConsumable())
                .stackable(product.getStackable())
                .durationHours(product.getDurationHours())
                .durability(product.getDurability())
                .cooldownHours(product.getCooldownHours())
                .active(product.getActive())
                .sellable(product.getSellable())
                .tradable(product.getTradable())
                .season(product.getSeason())
                .isEventItem(product.getIsEventItem())
                .eventStartDate(product.getEventStartDate())
                .eventEndDate(product.getEventEndDate())
                .tags(product.getTags())
                .instantEffects(null)
                .passiveEffects(null)
                .requirements(product.getRequirements())
                .totalUsageCount(product.getTotalUsageCount())
                .rating(product.getRating())
                .averageRating(calculateAverageRating(product))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Convierte Product a ProductDTO CON efectos (para detalle)
     */
    /**
     * Convierte Product a ProductDTO CON efectos (para detalle)
     */
    private ProductDTO convertToDtoWithEffects(Product product) {
        if (product == null) return null;

        // Convertir efectos instantáneos - asegurarse de no crear ciclos
        List<ProductInstantEffectDTO> instantEffectsDTO = null;
        if (product.getInstantEffects() != null && !product.getInstantEffects().isEmpty()) {
            instantEffectsDTO = product.getInstantEffects().stream()
                    .map(effect -> ProductInstantEffectDTO.builder()
                            .id(effect.getId())
                            .type(EffectTypeDTO.valueOf(effect.getType().name()))
                            .amount(effect.getAmount())
                            .isPercentage(effect.getIsPercentage())
                            .target(effect.getTarget())
                            .isRandom(effect.getIsRandom())
                            .minAmount(effect.getMinAmount())
                            .maxAmount(effect.getMaxAmount())
                            .description(effect.getDescription())
                            .formattedDescription(formatInstantEffect(effect))
                            .build())
                    .collect(Collectors.toList());
        }

        // Convertir efectos pasivos
        List<ProductPassiveEffectDTO> passiveEffectsDTO = null;
        if (product.getPassiveEffects() != null && !product.getPassiveEffects().isEmpty()) {
            passiveEffectsDTO = product.getPassiveEffects().stream()
                    .map(effect -> ProductPassiveEffectDTO.builder()
                            .id(effect.getId())
                            .stat(effect.getStat())
                            .value(effect.getValue())
                            .isPercentage(effect.getIsPercentage())
                            .trigger(TriggerConditionDTO.valueOf(effect.getTrigger().name()))
                            .condition(effect.getCondition())
                            .conditionValue(effect.getConditionValue())
                            .chancePercentage(effect.getChancePercentage())
                            .description(effect.getDescription())
                            .formattedDescription(formatPassiveEffect(effect))
                            .build())
                    .collect(Collectors.toList());
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .img(product.getImg())
                .icon(product.getIcon())
                .pa(product.getPa())
                .category(product.getCategory())
                .usageType(product.getUsageType())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .discountStartDate(product.getDiscountStartDate())
                .discountEndDate(product.getDiscountEndDate())
                .finalPrice(calculateFinalPrice(product))
                .rarity(mapRarityToDTO(product.getRarity()))
                .requiredLevel(product.getRequiredLevel())
                .requiredXp(product.getRequiredXp())
                .slot(mapSlotToDTO(product.getSlot()))
                .isConsumable(product.getIsConsumable())
                .stackable(product.getStackable())
                .durationHours(product.getDurationHours())
                .durability(product.getDurability())
                .cooldownHours(product.getCooldownHours())
                .active(product.getActive())
                .sellable(product.getSellable())
                .tradable(product.getTradable())
                .season(product.getSeason())
                .isEventItem(product.getIsEventItem())
                .eventStartDate(product.getEventStartDate())
                .eventEndDate(product.getEventEndDate())
                .tags(product.getTags() != null ? new ArrayList<>(product.getTags()) : null)
                .instantEffects(instantEffectsDTO)
                .passiveEffects(passiveEffectsDTO)
                .requirements(product.getRequirements() != null ? new ArrayList<>(product.getRequirements()) : null)
                .totalUsageCount(product.getTotalUsageCount())
                .rating(product.getRating())
                .averageRating(calculateAverageRating(product))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private String formatInstantEffect(ProductInstantEffect effect) {
        if (effect == null) return "";
        String prefix = effect.getIsPercentage() && effect.getAmount() != null && effect.getAmount() > 0 ? "+" : "";
        String suffix = effect.getIsPercentage() ? "%" : "";
        String effectName = getEffectDisplayName(effect.getType());

        if (effect.getIsRandom() && effect.getMinAmount() != null && effect.getMaxAmount() != null) {
            return String.format("%s: %d-%d%s", effectName, effect.getMinAmount(), effect.getMaxAmount(), suffix);
        }

        Integer amount = effect.getAmount() != null ? effect.getAmount() : 0;
        String sign = amount > 0 ? "+" : "";
        return String.format("%s: %s%d%s", effectName, sign, amount, suffix);
    }

    private String formatPassiveEffect(ProductPassiveEffect effect) {
        if (effect == null) return "";
        String prefix = effect.getIsPercentage() && effect.getValue() != null && effect.getValue() > 0 ? "+" : "";
        String suffix = effect.getIsPercentage() ? "%" : "";
        String triggerText = effect.getTrigger() != null ? effect.getTrigger().getDisplayName() : "Siempre";

        String effectText = String.format("%s %s%d%s", effect.getStat(), prefix, effect.getValue(), suffix);

        if (effect.getChancePercentage() != null && effect.getChancePercentage() < 100) {
            effectText = String.format("%s (%d%% de probabilidad)", effectText, effect.getChancePercentage());
        }

        return String.format("%s - %s", effectText, triggerText);
    }

    private String getEffectDisplayName(EffectType type) {
        if (type == null) return "";
        switch (type) {
            case HEALTH_RESTORE: return "Salud";
            case ENERGY_RESTORE: return "Energía";
            case STRESS_REDUCTION: return "Reducción de estrés";
            case XP_ACADEMY_BOOST: return "Bonus XP Académico";
            case XP_JOB_BOOST: return "Bonus XP Laboral";
            case TRAVEL_SPEED: return "Velocidad de viaje";
            case STUDY_EFFICIENCY: return "Eficiencia de estudio";
            case WORK_EFFICIENCY: return "Eficiencia laboral";
            case SALARY_BONUS: return "Bonus salarial";
            case HAPPINESS_BOOST: return "Felicidad";
            case DOUBLE_XP: return "Doble XP";
            case INTELLIGENCE_BOOST: return "Inteligencia";
            case CHARISMA_BOOST: return "Carisma";
            case CREATIVITY_BOOST: return "Creatividad";
            case RESILIENCE_BOOST: return "Resiliencia";
            default: return type.name();
        }
    }

    private BigDecimal calculateFinalPrice(Product product) {
        if (product == null) return BigDecimal.ZERO;
        LocalDate now = LocalDate.now();
        if (product.getDiscountPrice() != null &&
                product.getDiscountStartDate() != null &&
                product.getDiscountEndDate() != null &&
                !now.isBefore(product.getDiscountStartDate()) &&
                !now.isAfter(product.getDiscountEndDate())) {
            return product.getDiscountPrice();
        }
        return product.getPrice();
    }

    private Double calculateAverageRating(Product product) {
        if (product == null || product.getRatingCount() == null || product.getRatingCount() == 0) {
            return 0.0;
        }
        return (double) product.getRating() / product.getRatingCount();
    }

    private ProductRarityDTO mapRarityToDTO(ProductRarity rarity) {
        if (rarity == null) return ProductRarityDTO.COMMON;
        return ProductRarityDTO.valueOf(rarity.name());
    }

    private ProductSlotDTO mapSlotToDTO(ProductSlot slot) {
        if (slot == null) return ProductSlotDTO.NONE;
        return ProductSlotDTO.valueOf(slot.name());
    }
}