package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.JobPositionDTO;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;

import java.util.List;

public interface JobPositionService {

    // ===== CRUD BÁSICO =====
    JobPositionDTO create(JobPositionDTO dto);
    JobPositionDTO update(Long id, JobPositionDTO dto);
    JobPositionDTO getById(Long id);
    void delete(Long id);
    void deactivate(Long id);

    // ===== LISTADOS =====
    List<JobPositionDTO> getAll();
    List<JobPositionDTO> getAllActive();
    List<JobPositionDTO> getByCompany(Long companyId);
    List<JobPositionDTO> getByCompanyActive(Long companyId);
    List<JobPositionDTO> getByCategory(JobCategory category);
    List<JobPositionDTO> getByLevel(String level);
    List<JobPositionDTO> getByCareerPath(String careerPath);
    List<JobPositionDTO> getByTitle(String title);

    // ===== VACANTES RELACIONADAS =====
    List<JobPositionDTO> getPositionsWithActiveVacancies();
    Integer countActiveVacanciesByPosition(Long positionId);

    // ===== VALIDACIONES =====
    boolean existsByCompanyAndTitle(Long companyId, String title);
}