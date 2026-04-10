package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.JobApplicationEntity;
import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.jobservice.mapper.JobApplicationMapper;
import com.pet.businessdomain.jobservice.repository.JobApplicationRepository;
import com.pet.businessdomain.jobservice.repository.JobVacancyRepository;
import com.pet.businessdomain.jobservice.services.JobApplicationService;
import com.pet.businessdomain.shareddto.dto.JobApplicationDTO;
import com.pet.businessdomain.shareddto.dto.JobApplicationRequestDTO;
import com.pet.businessdomain.shareddto.dto.JobApplicationResultDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobVacancyRepository jobVacancyRepository;
    private final JobApplicationMapper jobApplicationMapper;

    @Override
    @Transactional
    public JobApplicationResultDTO apply(JobApplicationRequestDTO request) {
        log.info("Character {} applying to vacancy {}", request.getCharacterId(), request.getVacancyId());

        JobApplicationResultDTO result = new JobApplicationResultDTO();

        // Verificar si ya aplicó
        if (hasApplied(request.getCharacterId(), request.getVacancyId())) {
            result.setSuccess(false);
            result.setMessage("Ya has aplicado a esta vacante");
            return result;
        }

        // Verificar si la vacante está activa
        JobVacancyEntity vacancy = jobVacancyRepository.findById(request.getVacancyId())
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        if (!vacancy.getActive() || vacancy.getAvailableSlots() <= 0) {
            result.setSuccess(false);
            result.setMessage("La vacante ya no está disponible");
            return result;
        }

        // Calcular match score (simplificado)
        Integer matchScore = calculateMatchScore(request.getCharacterId(), request.getVacancyId());

        JobApplicationEntity entity = new JobApplicationEntity();
        entity.setCharacterId(request.getCharacterId());
        entity.setVacancy(vacancy);
        entity.setMatchScore(matchScore);

        JobApplicationEntity saved = jobApplicationRepository.save(entity);

        result.setSuccess(true);
        result.setMessage("Solicitud enviada correctamente");
        result.setApplication(jobApplicationMapper.toDto(saved));
        result.setMatchScore(matchScore);
        result.setMatchedSkills(new ArrayList<>());
        result.setMissingRequirements(new ArrayList<>());
        result.setRecommendations(new ArrayList<>());

        return result;
    }

    private Integer calculateMatchScore(Long characterId, Long vacancyId) {
        // TODO: Implementar lógica real de match entre personaje y requisitos
        return 50 + (int)(Math.random() * 50); // Placeholder
    }

    @Override
    public JobApplicationDTO getById(Long id) {
        return jobApplicationRepository.findById(id)
                .map(jobApplicationMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    @Override
    @Transactional
    public void withdraw(Long id) {
        log.info("Withdrawing application with id: {}", id);
        JobApplicationEntity entity = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        entity.setStatus(EnumAll.ApplicationStatus.WITHDRAWN);
        jobApplicationRepository.save(entity);
    }

    @Override
    public List<JobApplicationDTO> getByCharacter(Long characterId) {
        return jobApplicationMapper.toDtoList(jobApplicationRepository.findByCharacterId(characterId));
    }

    @Override
    public List<JobApplicationDTO> getByVacancy(Long vacancyId) {
        return jobApplicationMapper.toDtoList(jobApplicationRepository.findByVacancyId(vacancyId));
    }

    @Override
    public List<JobApplicationDTO> getByStatus(String status) {
        return jobApplicationMapper.toDtoList(jobApplicationRepository.findByStatus(status));
    }

    @Override
    public List<JobApplicationDTO> getPendingReview() {
        return jobApplicationMapper.toDtoList(jobApplicationRepository.findByStatusIn(List.of("PENDING", "REVIEWING")));
    }

    @Override
    public List<JobApplicationDTO> getRecentByCharacter(Long characterId) {
        return jobApplicationMapper.toDtoList(jobApplicationRepository.findTop10ByCharacterIdOrderByAppliedAtDesc(characterId));
    }

    @Override
    @Transactional
    public JobApplicationDTO reviewApplication(Long applicationId) {
        log.info("Reviewing application with id: {}", applicationId);

        JobApplicationEntity entity = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        entity.setStatus(EnumAll.ApplicationStatus.REVIEWING);
        entity.setStage(EnumAll.ApplicationStage.CV_REVIEW);

        return jobApplicationMapper.toDto(jobApplicationRepository.save(entity));
    }

    @Override
    @Transactional
    public JobApplicationDTO scheduleInterview(Long applicationId, LocalDateTime interviewDate) {
        log.info("Scheduling interview for application {} at {}", applicationId, interviewDate);

        jobApplicationRepository.scheduleInterview(applicationId, interviewDate, "TECHNICAL_INTERVIEW");

        return getById(applicationId);
    }

    @Override
    @Transactional
    public JobApplicationDTO updateInterviewResult(Long applicationId, String result, String notes) {
        log.info("Updating interview result for application {}: {}", applicationId, result);

        JobApplicationEntity entity = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        entity.setInterviewNotes(notes);

        if ("PASSED".equalsIgnoreCase(result)) {
            entity.setStage(EnumAll.ApplicationStage.HR_INTERVIEW);
        } else if ("FAILED".equalsIgnoreCase(result)) {
            entity.setStatus(EnumAll.ApplicationStatus.REJECTED);
            entity.setStage(null);
        }

        return jobApplicationMapper.toDto(jobApplicationRepository.save(entity));
    }

    @Override
    @Transactional
    public JobApplicationDTO makeOffer(Long applicationId, BigDecimal salary) {
        log.info("Making offer for application {} with salary {}", applicationId, salary);

        JobApplicationEntity entity = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        entity.setStatus(EnumAll.ApplicationStatus.OFFERED);
        entity.setStage(EnumAll.ApplicationStage.OFFER);

        return jobApplicationMapper.toDto(jobApplicationRepository.save(entity));
    }

    @Override
    @Transactional
    public JobApplicationDTO hire(Long applicationId) {
        log.info("Hiring candidate for application {}", applicationId);

        JobApplicationEntity entity = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        entity.setStatus(EnumAll.ApplicationStatus.HIRED);
        entity.setStage(EnumAll.ApplicationStage.HIRED);

        // Reducir slots disponibles de la vacante
        JobVacancyEntity vacancy = entity.getVacancy();
        vacancy.setAvailableSlots(vacancy.getAvailableSlots() - 1);
        jobVacancyRepository.save(vacancy);

        return jobApplicationMapper.toDto(jobApplicationRepository.save(entity));
    }

    @Override
    @Transactional
    public JobApplicationDTO reject(Long applicationId, String reason) {
        log.info("Rejecting application {}: {}", applicationId, reason);

        JobApplicationEntity entity = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        entity.setStatus(EnumAll.ApplicationStatus.REJECTED);
        entity.setStage(null);
        entity.setInterviewNotes(reason);

        return jobApplicationMapper.toDto(jobApplicationRepository.save(entity));
    }

    @Override
    public boolean hasApplied(Long characterId, Long vacancyId) {
        return jobApplicationRepository.existsByCharacterIdAndVacancyId(characterId, vacancyId);
    }

    @Override
    public Integer getMatchScore(Long characterId, Long vacancyId) {
        return jobApplicationRepository.findByCharacterIdAndVacancyId(characterId, vacancyId)
                .stream()
                .findFirst()
                .map(JobApplicationEntity::getMatchScore)
                .orElse(0);
    }

    @Override
    public Long countByCharacterAndStatus(Long characterId, String status) {
        return jobApplicationRepository.countByCharacterIdAndStatus(characterId, status);
    }

    @Override
    public Integer countByVacancy(Long vacancyId) {
        return jobApplicationRepository.countByVacancyId(vacancyId);
    }
}
