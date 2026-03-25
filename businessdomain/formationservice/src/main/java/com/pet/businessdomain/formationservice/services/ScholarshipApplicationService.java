package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.shareddto.dto.ScholarshipApplicationDto;

import java.util.List;

public interface ScholarshipApplicationService {

    ScholarshipApplicationDto applyToScholarship(Long scholarshipId, Long characterId);

    List<ScholarshipApplicationDto> getApplicationsByCharacter(Long characterId);

    List<ScholarshipApplicationDto> getApplicationsByScholarship(Long scholarshipId);

    ScholarshipApplicationDto updateStatus(Long applicationId, String status);
}