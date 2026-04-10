package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.RequirementDTO;

import java.util.List;

public interface RequirementService {

    RequirementDTO create(RequirementDTO dto);
    RequirementDTO update(Long id, RequirementDTO dto);
    RequirementDTO getById(Long id);
    void delete(Long id);
    List<RequirementDTO> getByVacancyId(Long vacancyId);
    void deleteByVacancyId(Long vacancyId);
    List<RequirementDTO> createBatch(List<RequirementDTO> dtos);
}