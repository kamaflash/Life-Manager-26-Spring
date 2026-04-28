package com.pet.businessdomain.productservice.mapper;

import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.productservice.entities.ProductShopOffer;
import com.pet.businessdomain.shareddto.dto.products.ProductShopOfferDTO;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductShopOfferMapper {

    // ========== ENTITY → DTO ==========
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapToProduct")
    @Mapping(target = "discountPercentage", source = ".", qualifiedByName = "calculateDiscountPercentage")
    @Mapping(target = "isAvailable", source = ".", qualifiedByName = "calculateIsAvailable")
    ProductShopOfferDTO toDto(ProductShopOffer entity);

    List<ProductShopOfferDTO> toDtoList(List<ProductShopOffer> entities);

    // ========== DTO → ENTITY ==========
    @Mapping(target = "id", ignore = true)
    ProductShopOffer toEntity(ProductShopOfferDTO dto);

    // ========== UPDATE ==========
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(ProductShopOfferDTO dto, @MappingTarget ProductShopOffer entity);

    // ========== QUALIFIERS ==========
    @Named("calculateDiscountPercentage")
    default BigDecimal calculateDiscountPercentage(ProductShopOffer offer) {
        if (offer == null || offer.getOriginalPrice() == null || offer.getOriginalPrice().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = offer.getOriginalPrice().subtract(offer.getOfferPrice());
        return discount.multiply(BigDecimal.valueOf(100))
                .divide(offer.getOriginalPrice(), 0, java.math.RoundingMode.HALF_UP);
    }

    @Named("calculateIsAvailable")
    default Boolean calculateIsAvailable(ProductShopOffer offer) {
        if (offer == null || !offer.getActive()) return false;

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(offer.getStartsAt()) || now.isAfter(offer.getExpiresAt())) return false;

        if (offer.getStockLimit() != -1 && offer.getRemainingStock() != null && offer.getRemainingStock() <= 0) {
            return false;
        }

        return true;
    }

    @Named("mapToProduct")
    default Product mapToProduct(Long productId) {
        return null;
    }
}