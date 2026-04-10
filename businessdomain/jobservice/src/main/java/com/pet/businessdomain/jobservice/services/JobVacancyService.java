package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.JobVacancyDTO;
import com.pet.businessdomain.shareddto.dto.JobSearchFiltersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface JobVacancyService {

    // ===== CRUD BÁSICO =====
    JobVacancyDTO create(JobVacancyDTO dto);
    JobVacancyDTO update(Long id, JobVacancyDTO dto);
    JobVacancyDTO getById(Long id);
    void delete(Long id);
    void closeVacancy(Long id);

    // ===== LISTADOS =====
    List<JobVacancyDTO> getAll();
    List<JobVacancyDTO> getAllActive();
    List<JobVacancyDTO> getByPosition(Long positionId);
    List<JobVacancyDTO> getByPositionActive(Long positionId);
    List<JobVacancyDTO> getByCompany(Long companyId);
    List<JobVacancyDTO> getByContractType(String contractType);
    List<JobVacancyDTO> getByWorkModality(String workModality);
    List<JobVacancyDTO> getBySalaryRange(BigDecimal min, BigDecimal max);

    // ===== BÚSQUEDAS AVANZADAS =====
    List<JobVacancyDTO> search(JobSearchFiltersDTO filters);
    Page<JobVacancyDTO> search(JobSearchFiltersDTO filters, Pageable pageable);
    List<JobVacancyDTO> findExpiringSoon();

    // ===== ESTADÍSTICAS =====
    Integer countApplicants(Long vacancyId);
    Integer getAvailableSlots(Long vacancyId);
    boolean hasAvailableSlots(Long vacancyId);

    // ===== VALIDACIONES =====
    boolean isExpired(Long vacancyId);
    boolean isActive(Long vacancyId);

    // ✅ Añadir método batch
    default List<JobVacancyDTO> createBatch(List<JobVacancyDTO> dtos) {
        List<JobVacancyDTO> created = new ArrayList<>();
        for (JobVacancyDTO dto : dtos) {
            created.add(create(dto));
        }
        return created;
    }
}