package com.pet.businessdomain.shareddto.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CharacterJobDTO {
    private Long id;
    private Long characterId;

    // Cambia de vacancyId a vacancy (el objeto completo)
    private JobVacancyDTO vacancy; // <-- CAMBIADO: ahora se llama vacancy

    private String positionTitle;
    private String companyName;
    private String companyLogo;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;

    // Progreso
    private Integer performance;
    private Integer satisfaction;
    private Integer stressLevel;

    // Historial
    private Integer promotionsReceived;
    private Integer bonusesReceived;
    private LocalDateTime lastPromotionDate;
    private BigDecimal currentSalary;

    // Relaciones laborales
    private List<WorkRelationshipDTO> relationships;

    // Misiones activas
    private List<MissionDTO> activeMissions;
}