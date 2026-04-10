package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.JobApplicationDTO;
import com.pet.businessdomain.shareddto.dto.JobApplicationRequestDTO;
import com.pet.businessdomain.shareddto.dto.JobApplicationResultDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface JobApplicationService {

    // ===== CRUD BÁSICO =====
    JobApplicationResultDTO apply(JobApplicationRequestDTO request);
    JobApplicationDTO getById(Long id);
    void withdraw(Long id);

    // ===== LISTADOS =====
    List<JobApplicationDTO> getByCharacter(Long characterId);
    List<JobApplicationDTO> getByVacancy(Long vacancyId);
    List<JobApplicationDTO> getByStatus(String status);
    List<JobApplicationDTO> getPendingReview();
    List<JobApplicationDTO> getRecentByCharacter(Long characterId);

    // ===== PROCESO DE SELECCIÓN =====
    JobApplicationDTO reviewApplication(Long applicationId);
    JobApplicationDTO scheduleInterview(Long applicationId, LocalDateTime interviewDate);
    JobApplicationDTO updateInterviewResult(Long applicationId, String result, String notes);
    JobApplicationDTO makeOffer(Long applicationId, BigDecimal salary);
    JobApplicationDTO hire(Long applicationId);
    JobApplicationDTO reject(Long applicationId, String reason);

    // ===== VERIFICACIONES =====
    boolean hasApplied(Long characterId, Long vacancyId);
    Integer getMatchScore(Long characterId, Long vacancyId);

    // ===== ESTADÍSTICAS =====
    Long countByCharacterAndStatus(Long characterId, String status);
    Integer countByVacancy(Long vacancyId);
}