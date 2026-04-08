package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.shareddto.dto.ScholarshipApplicationDto;
import com.pet.businessdomain.shareddto.enumentities.EnumFormation;

import java.util.List;

public interface ScholarshipApplicationService {

    ScholarshipApplicationDto applyToScholarship(Long scholarshipId, Long characterId);

    List<ScholarshipApplicationDto> getApplicationsByCharacter(Long characterId);

    List<ScholarshipApplicationDto> getApplicationsByScholarship(Long scholarshipId);

    ScholarshipApplicationDto updateStatus(Long applicationId, EnumFormation.ApplicationStatus statuss);
    boolean hasApplied(Long scholarshipId, Long characterId);
}