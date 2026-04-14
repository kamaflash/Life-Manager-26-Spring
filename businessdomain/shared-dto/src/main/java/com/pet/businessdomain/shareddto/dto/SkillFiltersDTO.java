package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillFiltersDTO {

    // ===== BÚSQUEDA GLOBAL =====
    private String search;  // Búsqueda global por nombre de habilidad

    // ===== FILTROS POR TEXTO EXACTO (LISTAS) =====
    private List<String> skills;        // Habilidades específicas (ej: ["basic_computer", "math_logic"])
    private List<String> categories;    // Categorías de habilidades
    private List<String> levels;        // Niveles específicos (ej: ["1", "2", "3"])

    // ===== FILTROS POR RANGO =====
    private Integer minLevel;           // Nivel mínimo
    private Integer maxLevel;           // Nivel máximo
    private Integer minProgress;        // Progreso mínimo (%)
    private Integer maxProgress;        // Progreso máximo (%)
    private Integer minXp;              // XP mínimo
    private Integer maxXp;              // XP máximo

    // ===== FILTROS DE ESTADO =====
    private Boolean locked;             // Bloqueada (true=bloqueada, false=disponible)
    private Boolean hasXp;              // Tiene XP (true=con XP, false=sin XP)

    // ===== PAGINACIÓN Y ORDENAMIENTO =====
    private int page = 0;
    private int size = 10;
    private String sortBy = "keyValue";
    private String sortDir = "asc";

    // ===== FILTRO ADICIONAL =====
    private Long characterId;

    // ===== MÉTODOS HELPER =====

    /**
     * Obtiene el nivel mínimo como Integer (para compatibilidad)
     */
    public Integer getMinLevel() {
        return minLevel;
    }

    /**
     * Obtiene el nivel máximo como Integer (para compatibilidad)
     */
    public Integer getMaxLevel() {
        return maxLevel;
    }

    /**
     * Verifica si hay filtro de habilidades específicas
     */
    public boolean hasSkillsFilter() {
        return skills != null && !skills.isEmpty();
    }

    /**
     * Verifica si hay filtro de categorías
     */
    public boolean hasCategoriesFilter() {
        return categories != null && !categories.isEmpty();
    }

    /**
     * Verifica si hay filtro de niveles
     */
    public boolean hasLevelsFilter() {
        return levels != null && !levels.isEmpty();
    }

    /**
     * Verifica si hay filtro de rango de niveles
     */
    public boolean hasLevelRangeFilter() {
        return (minLevel != null && minLevel > 0) || (maxLevel != null && maxLevel > 0);
    }

    /**
     * Verifica si hay filtro de progreso
     */
    public boolean hasProgressFilter() {
        return (minProgress != null && minProgress > 0) || (maxProgress != null && maxProgress > 0);
    }

    /**
     * Obtiene el primer nivel (para compatibilidad)
     */
    public String getFirstLevel() {
        if (levels != null && !levels.isEmpty()) {
            return levels.get(0);
        }
        return null;
    }

    /**
     * Obtiene la primera habilidad (para compatibilidad)
     */
    public String getFirstSkill() {
        if (skills != null && !skills.isEmpty()) {
            return skills.get(0);
        }
        return null;
    }

    /**
     * Limpia filtros vacíos (para optimización)
     */
    public void cleanEmptyFilters() {
        if (skills != null && skills.isEmpty()) skills = null;
        if (categories != null && categories.isEmpty()) categories = null;
        if (levels != null && levels.isEmpty()) levels = null;
        if (minLevel != null && minLevel <= 0) minLevel = null;
        if (maxLevel != null && maxLevel <= 0) maxLevel = null;
        if (minProgress != null && minProgress <= 0) minProgress = null;
        if (maxProgress != null && maxProgress <= 0) maxProgress = null;
        if (minXp != null && minXp <= 0) minXp = null;
        if (maxXp != null && maxXp <= 0) maxXp = null;
        if (search != null && search.isEmpty()) search = null;
    }

    @Override
    public String toString() {
        return "SkillFiltersDTO{" +
                "characterId=" + characterId +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", sortDir='" + sortDir + '\'' +
                ", skills=" + skills +
                ", minLevel=" + minLevel +
                ", maxLevel=" + maxLevel +
                ", locked=" + locked +
                ", minProgress=" + minProgress +
                ", maxProgress=" + maxProgress +
                ", search='" + search + '\'' +
                '}';
    }
}