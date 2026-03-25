package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumFormation;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScholarshipApplicationDto {
    private Long id;
    private Long scholarshipId;
    private Long characterId;
    private EnumFormation.ApplicationStatus status;
    private LocalDate appliedAt;
}
