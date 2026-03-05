package com.pet.businessdomain.jobservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CharacterApplicationDto {
    private Long id;
    private Long characterId;      // ID del personaje
    private Long positionId;
    private Long vacancyId;        // ID de la vacante
    private LocalDateTime appliedAt;
    private String status;         // APPLIED, INTERVIEW, HIRED, REJECTED
    private List<String> matchedSkills;
    private Integer matchScore;
}
