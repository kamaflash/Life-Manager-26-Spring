package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.JobApplicationDTO;
import com.pet.businessdomain.shareddto.dto.JobApplicationResultDTO;
import com.pet.businessdomain.shareddto.dto.JobContractDTO;
import com.pet.businessdomain.shareddto.dto.JobApplicationRequestDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface JobApplicationService {

    JobApplicationResultDTO apply(JobApplicationRequestDTO request);

    JobApplicationDTO getById(Long id);

    void withdraw(Long id);

    List<JobApplicationDTO> getByCharacter(Long characterId);

    List<JobApplicationDTO> getByVacancy(Long vacancyId);

    List<JobApplicationDTO> getByStatus(String status);

    List<JobApplicationDTO> getByCharacterAndStatus(Long characterId, EnumAll.ApplicationStatus status);

    List<JobApplicationDTO> getPendingReview();

    List<JobApplicationDTO> getRecentByCharacter(Long characterId);

    JobApplicationDTO reviewApplication(Long applicationId);

    JobApplicationDTO scheduleInterview(Long applicationId, LocalDateTime interviewDate);

    JobApplicationDTO updateInterviewResult(Long applicationId, String result, String notes);

    JobApplicationDTO makeOffer(Long applicationId, BigDecimal salary);

    JobApplicationDTO hire(Long applicationId);

    JobApplicationDTO reject(Long applicationId, String reason);

    boolean hasApplied(Long characterId, Long vacancyId);

    Integer getMatchScore(Long characterId, Long vacancyId);

    Long countByCharacterAndStatus(Long characterId, String status);

    Integer countByVacancy(Long vacancyId);

    Map<String, Object> processPendingApplications(Long characterId, Integer minMatchScore);

    JobApplicationDTO processApplication(Long applicationId, Integer minMatchScore);

    Map<String, Object> processPendingInterviews(Long characterId);

    JobContractDTO generateContract(Long applicationId);

    List<JobContractDTO> getCharacterContracts(Long characterId);

    List<JobContractDTO> getCharacterContractsByStatus(Long characterId, String status);

    Map<String, Object> acceptContract(Long contractId);

    Map<String, Object> rejectContract(Long contractId);

    JobContractDTO getContractById(Long contractId);
}