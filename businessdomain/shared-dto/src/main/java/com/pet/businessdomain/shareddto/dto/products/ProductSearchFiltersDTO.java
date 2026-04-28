package com.pet.businessdomain.shareddto.dto.products;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para los filtros de búsqueda de productos disponibles.
 *
 * <p>Contiene todos los criterios de filtrado y paginación para la búsqueda
 * de productos que un personaje puede comprar.</p>
 *
 * @author Life Manager Team
 * @version 1.0
 * @since 2026-04-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchFiltersDTO {

    // ========== BÚSQUEDA POR TEXTO ==========
    /** Término de búsqueda para filtrar por nombre o descripción */
    private String search;

    // ========== FILTROS POR CATEGORÍA ==========
    /** Categoría del producto (TRANSPORTE, SALUD, ALIMENTACION, etc.) */
    private ProductCategory category;

    /** Rareza del producto (COMMON, RARE, EPIC, LEGENDARY, MYTHIC) */
    private String rarity;

    /** Ranura del producto (VEHICLE, TOOL, HEAD, BODY, ACCESSORY, etc.) */
    private String slot;

    // ========== FILTROS POR NIVEL ==========
    /** Nivel mínimo requerido para usar/comprar el producto */
    private Integer minLevel;

    /** Nivel máximo requerido para usar/comprar el producto */
    private Integer maxLevel;

    // ========== FILTROS POR PRECIO ==========
    /** Precio mínimo del producto */
    private BigDecimal minPrice;

    /** Precio máximo del producto */
    private BigDecimal maxPrice;

    // ========== FILTROS POR TIPO ==========
    /** Si el producto es consumible (true) o reutilizable (false) */
    private Boolean isConsumable;

    /** Si el producto es de evento */
    private Boolean isEventItem;

    /** Temporada del producto (SPRING, SUMMER, AUTUMN, WINTER) */
    private String season;

    // ========== PAGINACIÓN ==========
    /** Número de página (desde 0) */
    private Integer page;

    /** Tamaño de página (número de elementos por página) */
    private Integer size;

    /** Campo por el que ordenar (name, price, requiredLevel, etc.) */
    private String sortBy;

    /** Dirección de ordenamiento (asc o desc) */
    private String sortDir;
}