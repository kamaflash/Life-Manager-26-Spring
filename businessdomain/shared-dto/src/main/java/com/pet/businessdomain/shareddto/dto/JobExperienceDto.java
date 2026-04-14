package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobExperienceDto {

    private Long id;

    // Empresa / Rol
    private String company;
    private String role;

    // Fechas
    private LocalDate startDate;
    private LocalDate endDate;

    // Economía / rendimiento
    private BigDecimal monthlySalary;
    private Integer performanceScore; // 0-100

    // Responsabilidades
    private List<String> responsibilities;

    // Skills ganadas
    private List<SkillXpDto> skillsGained;

    // Campos adicionales del contrato
    private String contractType;      // FULL_TIME, PART_TIME, etc.
    private String workModality;      // ONSITE, REMOTE, HYBRID
    private Integer weeklyHours;
    private String startTime;
    private String endTime;
    private Long applicationId;
    private Long vacancyId;
}