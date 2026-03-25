package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VacancyFullDto {

    private Long vacancyId;
    private Long positionId;
    private String positionTitle;
    private String description;

    // Campos simples de JobExperience
    private Integer minYearsExperience;
    private Integer minLevel;
    private Integer minXp;

    private String companyName;
    private BigDecimal salary;

    // Listas de skills, mapeadas manualmente en service
    private List<String> requiredSkills;
    private List<String> optionalSkills;

    public VacancyFullDto(Long vacancyId,
                          Long positionId,
                          String positionTitle,
                          String description,
                          Integer minYearsExperience,
                          Integer minLevel,
                          Integer minXp,
                          String companyName,
                          BigDecimal salary) {
        this.vacancyId = vacancyId;
        this.positionId = positionId;
        this.positionTitle = positionTitle;
        this.description = description;
        this.minYearsExperience = minYearsExperience;
        this.minLevel = minLevel;
        this.minXp = minXp;
        this.companyName = companyName;
        this.salary = salary;
    }
}
