package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.dto.*;
import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IJobService {

    // =======================
    // 🏢 EMPRESAS
    // =======================

    List<CompanyDto> getAllCompanies();

    CompanyDto getCompanyById(Long companyId);

    List<CompanyDto> getCompaniesByCategory(String category);

    CompanyDto createCompany(CompanyDto dto);

    CompanyDto updateCompany(Long id, CompanyDto dto);

    void deleteCompany(Long id);


    // =======================
    // 💼 PUESTOS DE TRABAJO
    // =======================

    List<JobPositionDto> getPositionsByCompany(Long companyId);

    JobPositionDto getPositionById(Long positionId);

    List<JobPositionDto> getPositionsByCategory(JobCategory category);
    Page<JobPositionEntity> getPositionsByCategoryPage(Pageable pageable, JobCategory category);

    JobPositionDto createPosition(JobPositionDto dto);

    JobPositionDto updatePosition(Long id, JobPositionDto dto);

    void deletePosition(Long id);


    // =======================
    // 📌 VACANTES
    // =======================

    List<JobVacancyDto> getVacanciesByPosition(Long positionId);

    List<VacancyFullDto> getAllFullVacancies();

    JobVacancyDto getVacancyById(Long vacancyId);

    JobVacancyDto createVacancy(JobVacancyDto dto);

    JobVacancyDto updateVacancy(Long id, JobVacancyDto dto);

    void deleteVacancy(Long id);


    // =======================
    // 📝 APLICACIONES
    // =======================

    List<CharacterApplicationDto> getApplicationsByCharacter(Long characterId);

    CharacterApplicationDto applyToVacancy(CharacterApplicationDto dto);

    void cancelApplication(Long applicationId);
    void cancelAll();


    // =======================
    // 🧠 LÓGICA AVANZADA
    // =======================

    /**
     * Devuelve solo las vacantes activas y compatibles
     * con el personaje (futuro filtro por XP, skills, education, etc.)
     */
    List<JobVacancyDto> getAvailableVacanciesForCharacter(Long characterId);
}
