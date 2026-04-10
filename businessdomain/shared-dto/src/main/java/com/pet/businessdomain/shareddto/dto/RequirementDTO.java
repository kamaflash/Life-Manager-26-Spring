package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class RequirementDTO {
    private Long id;
    private Long vacancyId;  // ✅ Añadir este campo
    private String type; // SKILL, STAT, EDUCATION, EXPERIENCE_YEARS, LEVEL
    private String skill_key; // "programming", "intelligence", "bachelor_degree"
    private Integer minValue;
    private Boolean mandatory;
    private Boolean met; // Para el personaje que consulta (si cumple o no)
}