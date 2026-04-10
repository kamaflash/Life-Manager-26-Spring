package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.CompanyDTO;
import com.pet.businessdomain.shareddto.dto.CompanySummaryDTO;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;

import java.util.List;

public interface CompanyService {

    // ===== CRUD BÁSICO =====
    CompanyDTO create(CompanyDTO dto);
    CompanyDTO update(Long id, CompanyDTO dto);
    CompanyDTO getById(Long id);
    CompanySummaryDTO getSummaryById(Long id);
    void delete(Long id);
    void deactivate(Long id);

    // ===== LISTADOS =====
    List<CompanyDTO> getAll();
    List<CompanyDTO> getAllActive();
    List<CompanySummaryDTO> getAllSummaries();
    List<CompanyDTO> getByCategory(JobCategory category);
    List<CompanyDTO> getByLocation(String location);
//    List<CompanyDTO> getByReputationGreaterThan(Integer reputation);
    List<CompanyDTO> getRemoteFriendly();
    List<CompanyDTO> getWithInternships();

    // ===== BÚSQUEDAS =====
    List<CompanyDTO> search(String name, JobCategory category, String location, Boolean active);
    List<CompanyDTO> findTopCompaniesByVacancies();

    // ===== ESTADÍSTICAS =====
    Integer countActiveVacanciesByCompany(Long companyId);
    Long countEmployees(Long companyId);
}