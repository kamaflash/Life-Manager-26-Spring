package com.pet.businessdomain.shareddto.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class JobSearchFiltersDTO {

    // ===== BÚSQUEDA GLOBAL =====
    private Object keyword;  // Puede ser String o List<String>
    private Long characterId;
    private Boolean onlyWeekendJobs;
    // ===== FILTROS POR TEXTO EXACTO (LISTAS) =====
    private List<String> positionTitles;   // Títulos de puesto
    private List<String> companyNames;     // Nombres de empresa
    private List<String> categories;       // Categorías (múltiples)
    private String category;               // Para compatibilidad (un solo valor)
    private List<String> contractTypes;    // Tipos de contrato
    private List<String> workModalities;   // Modalidades de trabajo
    private List<Integer> weeklyHours;     // Horas semanales

    // ===== FILTROS POR RANGO =====
    private String schedule;               // Horario (ej: "09:00 - 18:00")
    private Integer availableSlots;        // Vacantes mínimas disponibles
    private BigDecimal minSalary;          // Salario mínimo
    private BigDecimal maxSalary;          // Salario máximo

    // ===== FILTROS DE UBICACIÓN =====
    private String location;               // Ubicación (ciudad)

    // ===== FILTROS DE ESTADO =====
    private Boolean active;

    // ===== PAGINACIÓN Y ORDENAMIENTO =====
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDir = "asc";

    // ===== MÉTODOS HELPER =====

    /**
     * Obtiene keyword como String (para búsqueda global)
     */
    public String getKeywordAsString() {
        if (keyword == null) return null;
        if (keyword instanceof String) return (String) keyword;
        if (keyword instanceof List) {
            List<?> list = (List<?>) keyword;
            if (list.isEmpty()) return null;
            return String.join(" ", list.stream().map(Object::toString).toArray(String[]::new));
        }
        return keyword.toString();
    }

    /**
     * Obtiene keyword como Lista
     */
    public List<String> getKeywordAsList() {
        if (keyword == null) return null;
        if (keyword instanceof List) return (List<String>) keyword;
        if (keyword instanceof String) return List.of((String) keyword);
        return null;
    }

    /**
     * Obtiene categorías como lista (prioriza categories sobre category)
     */
    public List<String> getCategoriesList() {
        if (categories != null && !categories.isEmpty()) {
            return categories;
        }
        if (category != null && !category.isEmpty()) {
            return List.of(category);
        }
        return null;
    }

    /**
     * Obtiene el primer título de puesto (para compatibilidad)
     */
    public String getFirstPositionTitle() {
        if (positionTitles != null && !positionTitles.isEmpty()) {
            return positionTitles.get(0);
        }
        return null;
    }

    /**
     * Obtiene el primer nombre de empresa (para compatibilidad)
     */
    public String getFirstCompanyName() {
        if (companyNames != null && !companyNames.isEmpty()) {
            return companyNames.get(0);
        }
        return null;
    }

    /**
     * Obtiene el primer tipo de contrato (para compatibilidad)
     */
    public String getFirstContractType() {
        if (contractTypes != null && !contractTypes.isEmpty()) {
            return contractTypes.get(0);
        }
        return null;
    }

    /**
     * Obtiene la primera modalidad (para compatibilidad)
     */
    public String getFirstWorkModality() {
        if (workModalities != null && !workModalities.isEmpty()) {
            return workModalities.get(0);
        }
        return null;
    }
}