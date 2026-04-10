package com.pet.businessdomain.shareddto.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class JobVacancyDTO {
    private Long id;
    private Long positionId;
    private String positionTitle;
    private String companyName;
    private String companyLogo;

    // Salario
    private BigDecimal minSalary;
    private BigDecimal maxSalary;

    // Estado
    private Boolean active;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Integer availableSlots;

    // Detalles
    private String contractType;
    private String workModality;
    private Boolean visaSponsorship;
    private Integer weeklyHours;
    private LocalTime startTime;
    private LocalTime endTime;

    // 🔥 NUEVO: Descripción
    private String description;

    private List<String> workingDays;
    private List<String> benefits;
    private List<RequirementDTO> requirements;

    // Estadísticas
    private Integer totalApplicants;
    private Integer matchScore;
}