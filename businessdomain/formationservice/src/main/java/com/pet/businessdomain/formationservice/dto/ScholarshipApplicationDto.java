package com.pet.businessdomain.formationservice.dto;

import com.pet.businessdomain.formationservice.entities.ScholarshipApplicationEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ScholarshipApplicationDto {
    private Long id;
    private Long scholarshipId;
    private Long characterId;
    private ScholarshipApplicationEntity.ApplicationStatus status;
    private LocalDate appliedAt;
}
