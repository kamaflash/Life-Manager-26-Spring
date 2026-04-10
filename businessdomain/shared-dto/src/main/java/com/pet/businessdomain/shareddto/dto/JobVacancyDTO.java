package com.pet.businessdomain.shareddto.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private String location;
    private String contractType; // FULL_TIME, PART_TIME, INTERNSHIP, FREELANCE, TEMPORAL
    private String workModality; // ONSITE, REMOTE, HYBRID
    private Boolean visaSponsorship;
    private Integer weeklyHours;
    private List<String> workingDays; // MONDAY, TUESDAY...
    private List<String> benefits;

    // Requisitos
    private List<RequirementDTO> requirements;

    // Estadísticas de la vacante
    private Integer totalApplicants;
    private Integer matchScore; // Para el personaje que consulta
}
