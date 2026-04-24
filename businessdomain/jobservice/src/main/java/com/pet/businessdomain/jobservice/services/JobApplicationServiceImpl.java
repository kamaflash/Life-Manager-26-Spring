package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.*;
import com.pet.businessdomain.jobservice.mapper.JobApplicationMapper;
import com.pet.businessdomain.jobservice.repository.JobApplicationRepository;
import com.pet.businessdomain.jobservice.repository.JobContractRepository;
import com.pet.businessdomain.jobservice.repository.JobVacancyRepository;
import com.pet.businessdomain.jobservice.specifications.JobApplicationSpecifications;
import com.pet.businessdomain.jobservice.specifications.JobContractSpecifications;
import com.pet.businessdomain.jobservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobVacancyRepository jobVacancyRepository;
    private final JobContractRepository jobContractRepository;
    private final JobApplicationMapper jobApplicationMapper;
    private final CharacterJobService characterJobService;
    private final BusinessTransactions businessTransactions;

    @Override
    @Transactional
    public JobApplicationResultDTO apply(JobApplicationRequestDTO request) {
        log.info("Character {} applying to vacancy {}", request.getCharacterId(), request.getVacancyId());

        JobApplicationResultDTO result = new JobApplicationResultDTO();

        if (hasApplied(request.getCharacterId(), request.getVacancyId())) {
            result.setSuccess(false);
            result.setMessage("Ya has aplicado a esta vacante");
            return result;
        }

        JobVacancyEntity vacancy = jobVacancyRepository.findById(request.getVacancyId())
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        if (!vacancy.getActive() || vacancy.getAvailableSlots() <= 0) {
            result.setSuccess(false);
            result.setMessage("La vacante ya no está disponible");
            return result;
        }

        Integer matchScore = calculateMatchScore(request.getCharacterId(), request.getVacancyId());

        JobApplicationEntity entity = new JobApplicationEntity();
        entity.setCharacterId(request.getCharacterId());
        entity.setVacancy(vacancy);
        entity.setMatchScore(matchScore);
        entity.setStatus(EnumAll.ApplicationStatus.PENDING);
        entity.setStage(EnumAll.ApplicationStage.APPLICATION);
        JobApplicationEntity saved = jobApplicationRepository.save(entity);

        result.setSuccess(true);
        result.setMessage("Solicitud enviada correctamente");
        result.setApplication(jobApplicationMapper.toDto(saved));
        result.setMatchScore(matchScore);
        result.setMatchedSkills(new ArrayList<>());
        result.setMissingRequirements(new ArrayList<>());
        result.setRecommendations(new ArrayList<>());
        CharacterDto characterDto = businessTransactions.getCharacter(saved.getCharacterId());
        setNotification(characterDto, saved, false);
        SystemDto systemDto = businessTransactions.getSystem(characterDto.getId());
        systemDto.setPa(systemDto.getPa()-2);
        systemDto.setActualityAt(systemDto.getActualityAt().plusHours(2));
        systemDto = businessTransactions.updateSystem(systemDto.getUid(),systemDto.getActualityAt(),2);
        return result;
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
    public List<JobApplicationDTO> getByCharacterAndStatus(Long characterId, EnumAll.ApplicationStatus status) {
        log.info("Getting applications for character: {} with status: {}", characterId, status);

        List<JobApplicationEntity> applications = jobApplicationRepository
                .findByCharacterIdAndStatus(characterId, status);

        return applications.stream()
                .map(jobApplicationMapper::toDto)
                .collect(Collectors.toList());
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

    @Override
    @Transactional
    public Map<String, Object> processPendingApplications(Long characterId, Integer minMatchScore) {
        log.info("Processing pending applications for character: {}", characterId);

        List<JobApplicationEntity> pendingApplications = jobApplicationRepository
                .findByCharacterIdAndStatus(characterId, EnumAll.ApplicationStatus.PENDING);

        int accepted = 0;
        int rejected = 0;
        List<JobApplicationDTO> processedApplications = new ArrayList<>();

        for (JobApplicationEntity application : pendingApplications) {
            Integer matchScore = calculateMatchScore(characterId, application.getVacancy().getId());
            CharacterDto characterDto = businessTransactions.getCharacter(characterId);
            SystemDto systemDto = businessTransactions.getSystem(characterId);
            application.setMatchScore(matchScore);

            if (matchScore >= minMatchScore) {
                application.setStatus(EnumAll.ApplicationStatus.INTERVIEW_SCHEDULED);
                application.setStage(EnumAll.ApplicationStage.PHONE_SCREEN);
                application.setInterviewDate(systemDto.getActualityAt().plusDays(2));
                accepted++;
                log.info("Application {} ACCEPTED with match score: {}", application.getId(), matchScore);
            } else {
                application.setStatus(EnumAll.ApplicationStatus.REJECTED);
                application.setStage(EnumAll.ApplicationStage.APPLICATION);
                rejected++;
                log.info("Application {} REJECTED with match score: {}", application.getId(), matchScore);
            }
            setNotification(characterDto, application, true);
            jobApplicationRepository.save(application);
            processedApplications.add(jobApplicationMapper.toDto(application));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalProcessed", pendingApplications.size());
        result.put("accepted", accepted);
        result.put("rejected", rejected);
        result.put("minMatchScore", minMatchScore);
        result.put("applications", processedApplications);

        return result;
    }

    @Override
    @Transactional
    public JobApplicationDTO processApplication(Long applicationId, Integer minMatchScore) {
        log.info("Processing application: {}", applicationId);

        JobApplicationEntity application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));

        if (application.getStatus() != EnumAll.ApplicationStatus.PENDING) {
            throw new RuntimeException("Application already processed. Current status: " + application.getStatus());
        }

        Integer matchScore = calculateMatchScore(application.getCharacterId(), application.getVacancy().getId());
        application.setMatchScore(matchScore);

        if (matchScore >= minMatchScore) {
            application.setStatus(EnumAll.ApplicationStatus.INTERVIEW_SCHEDULED);
            application.setStage(EnumAll.ApplicationStage.PHONE_SCREEN);
            log.info("Application {} ACCEPTED - Match score: {}", applicationId, matchScore);
        } else {
            application.setStatus(EnumAll.ApplicationStatus.REJECTED);
            log.info("Application {} REJECTED - Match score: {}", applicationId, matchScore);
        }

        JobApplicationEntity saved = jobApplicationRepository.save(application);
        return jobApplicationMapper.toDto(saved);
    }

    @Override
    @Transactional
    public Map<String, Object> processPendingInterviews(Long characterId) {
        log.info("Processing pending interviews for character: {}", characterId);

        SystemDto systemDto = businessTransactions.getSystem(characterId);
        LocalDate targetDate = systemDto.getActualityAt().toLocalDate();

        List<JobApplicationEntity> scheduledInterviews = jobApplicationRepository
                .findByCharacterIdAndStatusAndInterviewDateBetween(
                        characterId,
                        EnumAll.ApplicationStatus.INTERVIEW_SCHEDULED,
                        targetDate.atStartOfDay(),
                        targetDate.plusDays(1).atStartOfDay()
                );

        if (scheduledInterviews.isEmpty()) {
            log.info("No pending interviews found for date: {}", targetDate);
            Map<String, Object> result = new HashMap<>();
            result.put("totalProcessed", 0);
            result.put("message", "No hay entrevistas programadas para la fecha: " + targetDate);
            return result;
        }

        CharacterDto characterDto = businessTransactions.getCharacter(characterId);
        int passed = 0;
        int failed = 0;
        List<JobApplicationDTO> processedApplications = new ArrayList<>();

        for (JobApplicationEntity application : scheduledInterviews) {
            log.info("Processing interview for application: {}", application.getId());

            boolean isHealthy = characterDto.getStats().getHealth() > 50;
            boolean isNotTooStressed = characterDto.getStats().getStress() < 80;

            if (isHealthy && isNotTooStressed) {
                application.setStatus(EnumAll.ApplicationStatus.OFFERED);
                application.setStage(EnumAll.ApplicationStage.OFFER);
                application.setInterviewNotes("El candidato superó la entrevista exitosamente. Salud: "
                        + characterDto.getStats().getHealth() + ", Estrés: " + characterDto.getStats().getStress());
                passed++;
                log.info("Interview PASSED for application {} - Health: {}, Stress: {}",
                        application.getId(), characterDto.getStats().getHealth(), characterDto.getStats().getStress());
                sendInterviewResultNotification(characterDto, application, true);
                generateContract(application.getId());
            } else {
                application.setStatus(EnumAll.ApplicationStatus.REJECTED);
                application.setStage(EnumAll.ApplicationStage.APPLICATION);

                String reason = "";
                if (!isHealthy && !isNotTooStressed) {
                    reason = "Salud baja (" + characterDto.getStats().getHealth() + ") y estrés alto (" + characterDto.getStats().getStress() + ")";
                } else if (!isHealthy) {
                    reason = "Salud baja (" + characterDto.getStats().getHealth() + ") - mínimo requerido: 50";
                } else {
                    reason = "Estrés alto (" + characterDto.getStats().getStress() + ") - máximo permitido: 80";
                }

                application.setInterviewNotes("El candidato no superó la entrevista. Razón: " + reason);
                failed++;
                log.info("Interview FAILED for application {} - Reason: {}", application.getId(), reason);
                sendInterviewResultNotification(characterDto, application, false);
            }

            jobApplicationRepository.save(application);
            processedApplications.add(jobApplicationMapper.toDto(application));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalProcessed", scheduledInterviews.size());
        result.put("passed", passed);
        result.put("failed", failed);
        result.put("interviewDate", targetDate);
        result.put("characterHealth", characterDto.getStats().getHealth());
        result.put("characterStress", characterDto.getStats().getStress());
        result.put("applications", processedApplications);

        log.info("Interviews processed - Total: {}, Passed: {}, Failed: {}",
                scheduledInterviews.size(), passed, failed);

        return result;
    }

    @Override
    @Transactional
    public JobContractDTO generateContract(Long applicationId) {
        log.info("=== START generateContract ===");
        log.info("Generating contract for application: {}", applicationId);

        JobApplicationEntity application = jobApplicationRepository.findByIdWithAllRelations(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));
        log.info("Application found - Status: {}", application.getStatus());

        if (application.getStatus() != EnumAll.ApplicationStatus.OFFERED) {
            throw new RuntimeException("Contract can only be generated for offered applications. Current status: " + application.getStatus());
        }

        Optional<JobContractEntity> existingContract = jobContractRepository.findByApplicationId(applicationId);
        log.info("Existing contract found: {}", existingContract.isPresent());

        if (existingContract.isPresent()) {
            log.info("Contract already exists for application: {}", applicationId);
            return convertToDTO(existingContract.get());
        }

        BigDecimal offeredSalary = calculateOfferedSalary(application);
        log.info("Offered salary calculated: {}", offeredSalary);

        application.setOfferedSalary(offeredSalary);
        jobApplicationRepository.save(application);
        log.info("Application saved with offeredSalary: {}", offeredSalary);

        JobContractEntity contractEntity = buildContractEntity(application, offeredSalary);
        log.info("Contract entity built - ContractNumber: {}, ApplicationId: {}",
                contractEntity.getContractNumber(), contractEntity.getApplicationId());

        JobContractEntity saved = jobContractRepository.save(contractEntity);
        log.info("Contract saved with ID: {}", saved.getId());

        return convertToDTO(saved);
    }

    @Override
    public List<JobContractDTO> getCharacterContracts(Long characterId) {
        log.info("Getting all contracts for character: {}", characterId);
        return jobContractRepository.findByCharacterId(characterId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobContractDTO> getCharacterContractsByStatus(Long characterId, String status) {
        log.info("Getting contracts for character: {} with status: {}", characterId, status);
        return jobContractRepository.findByCharacterIdAndStatus(characterId, status.toUpperCase())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> acceptContract(Long contractId) {
        log.info("Accepting contract: {}", contractId);

        JobContractEntity contract = jobContractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found: " + contractId));

        if (!"PENDING".equals(contract.getStatus())) {
            throw new RuntimeException("Contract cannot be accepted. Current status: " + contract.getStatus());
        }

        // Convertir contrato a experiencia laboral
        JobExperienceDto jobExperience = convertContractToJobExperience(contract);

        // Añadir experiencia laboral al personaje
        try {
            CharacterDto updatedCharacter = businessTransactions.addJobExperience(
                    contract.getCharacterId(),
                    jobExperience
            );
            log.info("Job experience added to character: {}", updatedCharacter.getId());
        } catch (Exception e) {
            log.error("Error adding job experience to character: {}", e.getMessage());
            throw new RuntimeException("Failed to add job experience to character", e);
        }

        contract.setStatus("ACCEPTED");
        contract.setAcceptedAt(LocalDateTime.now());
        jobContractRepository.save(contract);

        // Actualizar la aplicación original
        jobApplicationRepository.findById(contract.getApplicationId()).ifPresent(app -> {
            app.setStatus(EnumAll.ApplicationStatus.HIRED);
            app.setStage(EnumAll.ApplicationStage.HIRED);
            jobApplicationRepository.save(app);
        });

        // Enviar notificación
        sendContractAcceptedNotification(contract);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Contrato aceptado. ¡Felicidades por tu nuevo trabajo!");
        result.put("contractId", contractId);
        result.put("jobExperience", jobExperience);

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> rejectContract(Long contractId) {
        log.info("Rejecting contract: {}", contractId);

        JobContractEntity contract = jobContractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found: " + contractId));

        if (!"PENDING".equals(contract.getStatus())) {
            throw new RuntimeException("Contract cannot be rejected. Current status: " + contract.getStatus());
        }

        contract.setStatus("REJECTED");
        contract.setRejectedAt(LocalDateTime.now());
        jobContractRepository.save(contract);

        jobApplicationRepository.findById(contract.getApplicationId()).ifPresent(app -> {
            app.setStatus(EnumAll.ApplicationStatus.REJECTED);
            app.setStage(EnumAll.ApplicationStage.APPLICATION);
            jobApplicationRepository.save(app);
        });

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Contrato rechazado");
        result.put("contractId", contractId);

        return result;
    }

    @Override
    public JobContractDTO getContractById(Long contractId) {
        log.info("Getting contract by id: {}", contractId);
        JobContractEntity contract = jobContractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found: " + contractId));
        return convertToDTO(contract);
    }

    // ==================== MÉTODOS PRIVADOS ====================

    private void setNotification(CharacterDto dto, JobApplicationEntity application, boolean isProcessed) {
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setUserId(dto.getId());
        notificationDTO.setFromUserId(dto.getUid());
        notificationDTO.setType(NotificationType.JOBS);
        notificationDTO.setResourceType(NotificationResourceType.JOBS);
        notificationDTO.setResourceId(application.getId());
        notificationDTO.setRead(false);
        notificationDTO.setActionUrl("/profile");

        if (isProcessed) {
            if (application.getStatus() == EnumAll.ApplicationStatus.INTERVIEW_SCHEDULED) {
                notificationDTO.setTitle("🎉 ¡Buenas noticias! Tu postulación ha avanzado");
                notificationDTO.setSubTitle("Has sido preseleccionado para la siguiente fase");
                notificationDTO.setMessage(String.format(
                        "Tu postulación para la vacante ha sido aceptada con un match score de %d. " +
                                "Pronto recibirás más información para coordinar la entrevista.",
                        application.getMatchScore()
                ));
                notificationDTO.setEventType(NotificationEventType.JOB_OFFER);
            } else {
                notificationDTO.setTitle("😔 Actualización de tu postulación");
                notificationDTO.setSubTitle("Tu candidatura no ha sido seleccionada");
                notificationDTO.setMessage(String.format(
                        "Tu postulación no cumple con los requisitos mínimos (match score: %d). " +
                                "Te invitamos a seguir postulando a otras vacantes.",
                        application.getMatchScore()
                ));
                notificationDTO.setEventType(NotificationEventType.JOB_APPLICATION_REJECTED);
            }
        } else {
            notificationDTO.setTitle("📝 ¡Postulación recibida!");
            notificationDTO.setSubTitle("Tu solicitud ha sido enviada correctamente");
            notificationDTO.setMessage(String.format(
                    "Has aplicado exitosamente a la vacante. Tu match score preliminar es de %d. " +
                            "Estaremos revisando tu perfil y te notificaremos cualquier novedad.",
                    application.getMatchScore()
            ));
            notificationDTO.setEventType(NotificationEventType.JOB_APPLICATION_ACCEPTED);
        }

        notificationDTO.setMetadata(String.format("""
        {
            "characterId": %d,
            "applicationId": %d,
            "vacancyId": %d,
            "status": "%s",
            "matchScore": %d
        }
        """,
                dto.getId(),
                application.getId(),
                application.getVacancy().getId(),
                application.getStatus(),
                application.getMatchScore()
        ));

        businessTransactions.setNotifications(notificationDTO);
    }

    private void sendInterviewResultNotification(CharacterDto characterDto, JobApplicationEntity application, boolean passed) {
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setUserId(characterDto.getId());
        notificationDTO.setFromUserId(characterDto.getUid());
        notificationDTO.setType(NotificationType.JOBS);
        notificationDTO.setResourceType(NotificationResourceType.JOBS);
        notificationDTO.setResourceId(application.getId());
        notificationDTO.setRead(false);
        notificationDTO.setActionUrl("/profile");

        if (passed) {
            notificationDTO.setTitle("🎉 ¡Entrevista superada!");
            notificationDTO.setSubTitle("Has pasado a la siguiente fase del proceso");
            notificationDTO.setMessage(String.format(
                    "¡Enhorabuena! Has superado la entrevista. " +
                            "Tu salud (%d) y nivel de estrés (%d) estaban en óptimas condiciones. " +
                            "Pronto recibirás tu contrato.",
                    characterDto.getStats().getHealth(),
                    characterDto.getStats().getStress()
            ));
            notificationDTO.setEventType(NotificationEventType.JOB_OFFER);
        } else {
            notificationDTO.setTitle("😔 Entrevista no superada");
            notificationDTO.setSubTitle("No has pasado a la siguiente fase");
            notificationDTO.setMessage(String.format(
                    "Lamentamos informarte que no has superado la entrevista. " +
                            "Motivo: %s. Te recomendamos cuidar tu salud y reducir el estrés.",
                    application.getInterviewNotes()
            ));
            notificationDTO.setEventType(NotificationEventType.JOB_APPLICATION_REJECTED);
        }

        notificationDTO.setMetadata(String.format("""
        {
            "characterId": %d,
            "applicationId": %d,
            "status": "%s",
            "matchScore": %d,
            "health": %d,
            "stress": %d
        }
        """,
                characterDto.getId(),
                application.getId(),
                application.getStatus(),
                application.getMatchScore(),
                characterDto.getStats().getHealth(),
                characterDto.getStats().getStress()
        ));

        businessTransactions.setNotifications(notificationDTO);
    }

    private Integer calculateMatchScore(Long characterId, Long vacancyId) {
        CharacterDto character = businessTransactions.getCharacter(characterId);
        JobVacancyEntity vacancy = jobVacancyRepository.findByIdWithRequirements(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found: " + vacancyId));

        int totalScore = 0;
        int maxScore = 0;

        List<RequirementEntity> requirements = vacancy.getRequirements();
        if (!requirements.isEmpty()) {
            for (RequirementEntity req : requirements) {
                if ("SKILL".equals(req.getType())) {
                    maxScore += 40;
                    Integer characterSkillLevel = getCharacterSkillLevel(character, req.getSkill_key());
                    if (characterSkillLevel >= req.getMinValue()) {
                        totalScore += 40;
                    }
                }
            }
        } else {
            totalScore += 40;
            maxScore += 40;
        }

        maxScore += 30;
        int characterXp = (character.getXpAcademy() != null ? character.getXpAcademy() : 0)
                + (character.getXpJobs() != null ? character.getXpJobs() : 0);
        int requiredXp = getRequiredExperience(vacancy);
        if (characterXp >= requiredXp) {
            totalScore += 30;
            if (requiredXp == 0) {
                totalScore += 40;
            }
        } else if (characterXp >= requiredXp * 0.7) {
            totalScore += 15;
        }
        maxScore += 20;
        StatsDto stats = character.getStats();
        if (stats != null) {
            int statScore = 0;
            if (stats.getIntelligence() >= 50) statScore += 5;
            if (stats.getCharisma() >= 50) statScore += 5;
            if (stats.getCreativity() >= 50) statScore += 5;
            if (stats.getResilience() >= 50) statScore += 5;
            totalScore += statScore;
        }

        maxScore += 10;
        boolean hasSpecialEducation = hasEducationWithId(character, 104) || hasEducationWithId(character, 105);

        if (hasSpecialEducation) {
            boolean canMeetSchedule = canMeetWorkSchedule(character, vacancy);
            if (!canMeetSchedule) {
                log.info("Character {} cannot meet work schedule for vacancy {}", characterId, vacancyId);
                return 0;
            }
            if (hasRequiredEducation(character, vacancy)) {
                totalScore += 10;
            }
        } else {
            if (hasRequiredEducation(character, vacancy)) {
                totalScore += 10;
            }
        }

        return (totalScore * 100) / maxScore;
    }

    private boolean hasEducationWithId(CharacterDto character, int trainingId) {
        if (character.getEducation() == null || character.getEducation().isEmpty()) {
            return false;
        }
        return character.getEducation().stream()
                .anyMatch(edu -> edu.getTrainingId() != null && edu.getTrainingId() == trainingId);
    }

    private boolean canMeetWorkSchedule(CharacterDto character, JobVacancyEntity vacancy) {
        CharacterTrainingDto specialEducation = character.getEducation().stream()
                .filter(edu -> edu.getTrainingId() != null &&
                        (edu.getTrainingId() == 104 || edu.getTrainingId() == 105))
                .findFirst()
                .orElse(null);

        if (specialEducation == null) {
            return true;
        }

        FormationDto formationDto = businessTransactions.getFormation(specialEducation.getTrainingId());

        LocalTime formationStartTime = formationDto.getStartTime();
        LocalTime formationEndTime = formationDto.getEndTime();
        List<EnumAll.WorkingDay> formationWorkingDays = formationDto.getWorkingDays();

        LocalTime jobStartTime = vacancy.getStartTime();
        LocalTime jobEndTime = vacancy.getEndTime();
        List<EnumAll.WorkingDay> jobWorkingDays = vacancy.getWorkingDays();

        if (formationStartTime == null || formationEndTime == null || formationWorkingDays == null) {
            return true;
        }

        if (jobStartTime == null || jobEndTime == null || jobWorkingDays == null) {
            return true;
        }

        boolean hasTimeConflict = hasTimeConflict(formationStartTime, formationEndTime, jobStartTime, jobEndTime);
        boolean hasDayConflict = hasDayConflict(formationWorkingDays, jobWorkingDays);

        return !(hasTimeConflict && hasDayConflict);
    }

    private boolean hasTimeConflict(LocalTime formationStart, LocalTime formationEnd,
                                    LocalTime jobStart, LocalTime jobEnd) {
        boolean jobInsideFormation = (jobStart.isAfter(formationStart) || jobStart.equals(formationStart))
                && (jobEnd.isBefore(formationEnd) || jobEnd.equals(formationEnd));

        boolean formationInsideJob = (formationStart.isAfter(jobStart) || formationStart.equals(jobStart))
                && (formationEnd.isBefore(jobEnd) || formationEnd.equals(jobEnd));

        boolean overlap = jobStart.isBefore(formationEnd) && jobEnd.isAfter(formationStart);

        return jobInsideFormation || formationInsideJob || overlap;
    }

    private boolean hasDayConflict(List<EnumAll.WorkingDay> formationDays, List<EnumAll.WorkingDay> jobDays) {
        if (formationDays == null || formationDays.isEmpty() || jobDays == null || jobDays.isEmpty()) {
            return false;
        }

        for (EnumAll.WorkingDay jobDay : jobDays) {
            if (formationDays.contains(jobDay)) {
                return true;
            }
        }
        return false;
    }

    private Integer getCharacterSkillLevel(CharacterDto character, String skillKey) {
        if (character.getSkills() != null && character.getSkills().containsKey(skillKey)) {
            return character.getSkills().get(skillKey).getLevel();
        }
        return 0;
    }

    private Integer getRequiredExperience(JobVacancyEntity vacancy) {
        return vacancy.getRequirements().stream()
                .filter(req -> "EXPERIENCE_YEARS".equals(req.getType()))
                .findFirst()
                .map(RequirementEntity::getMinValue)
                .orElse(0);
    }

    private boolean hasRequiredEducation(CharacterDto character, JobVacancyEntity vacancy) {
        String requiredEducation = vacancy.getRequirements().stream()
                .filter(req -> "EDUCATION".equals(req.getType()))
                .findFirst()
                .map(RequirementEntity::getSkill_key)
                .orElse(null);

        if (requiredEducation == null || requiredEducation.isEmpty()) {
            return true;
        }

        Integer xpAcademy = character.getXpAcademy() != null ? character.getXpAcademy() : 0;

        Map<String, Integer> educationXpRequirements = Map.of(
                "NONE", 0, "BASIC", 10, "SECONDARY", 50,
                "HIGH_SCHOOL", 100, "VOCATIONAL", 150, "TECHNICAL", 150,
                "UNIVERSITY", 300, "MASTER", 500, "PHD", 800
        );

        int requiredXp = educationXpRequirements.getOrDefault(requiredEducation.toUpperCase(), 0);
        return xpAcademy >= requiredXp;
    }

    private BigDecimal calculateOfferedSalary(JobApplicationEntity application) {
        BigDecimal minSalary = application.getVacancy().getMinSalary();
        BigDecimal maxSalary = application.getVacancy().getMaxSalary();
        Integer matchScore = application.getMatchScore();

        if (matchScore == null || matchScore <= 50) {
            return minSalary;
        }

        if (matchScore >= 100) {
            return maxSalary;
        }

        BigDecimal range = maxSalary.subtract(minSalary);
        BigDecimal percentage = BigDecimal.valueOf(matchScore - 50)
                .divide(BigDecimal.valueOf(50), 4, RoundingMode.HALF_UP);

        return minSalary.add(range.multiply(percentage))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private JobContractEntity buildContractEntity(JobApplicationEntity application, BigDecimal offeredSalary) {
        JobVacancyEntity vacancy = application.getVacancy();
        JobPositionEntity position = vacancy.getPosition();
        CompanyEntity company = position.getCompany();
        CharacterDto characterDto = businessTransactions.getCharacter(application.getCharacterId());

        JobContractEntity contract = new JobContractEntity();

        // Datos obligatorios
        contract.setContractNumber(generateContractNumber(application, company));
        contract.setStatus("PENDING");
        contract.setContractUrl("/contracts/" + application.getId());
        contract.setIssuedAt(LocalDateTime.now());
        contract.setValidUntil(LocalDateTime.now().plusDays(7));

        // Datos del empleado
        contract.setCharacterId(characterDto.getId());
        contract.setCharacterName(characterDto.getName() != null ? characterDto.getName() : "Desconocido");
        contract.setCharacterAge(characterDto.getAge() != null ? characterDto.getAge() : 0);
        contract.setCharacterBirthDate(LocalDate.from(characterDto.getBirthDate()));

        // Datos de la empresa
        contract.setCompanyId(company.getId());
        contract.setCompanyName(company.getName() != null ? company.getName() : "Desconocida");
        contract.setCompanyAddress(company.getLocation());
        contract.setCompanyPhone(company.getPhone());
        contract.setCompanyEmail(company.getContactEmail());
        contract.setCompanyWebsite(company.getWebsite());

        // Datos del puesto
        contract.setPositionId(position.getId());
        contract.setPositionTitle(position.getTitle() != null ? position.getTitle() : "Puesto no especificado");
        contract.setPositionLevel(position.getLevel() != null ? position.getLevel().toString() : "JUNIOR");
        contract.setPositionCategory(position.getCategory());
        contract.setPositionDescription(position.getDescription());

        // Datos económicos
        contract.setBaseSalary(offeredSalary != null ? offeredSalary : BigDecimal.ZERO);
        contract.setMonthlySalary(offeredSalary != null ? offeredSalary.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        contract.setExtraPayments(BigDecimal.valueOf(2));
        contract.setSalaryCurrency("EUR");
        contract.setSalaryPeriod("ANNUAL");

        // Match score
        contract.setMatchScore(application.getMatchScore() != null ? application.getMatchScore() : 0);
        contract.setMinSalaryRange(vacancy.getMinSalary());
        contract.setMaxSalaryRange(vacancy.getMaxSalary());

        // Jornada
        contract.setContractType(vacancy.getContractType() != null ? vacancy.getContractType().toString() : "FULL_TIME");
        contract.setWorkModality(vacancy.getWorkModality() != null ? vacancy.getWorkModality().toString() : "ONSITE");
        contract.setWeeklyHours(vacancy.getWeeklyHours() != null ? vacancy.getWeeklyHours() : 40);
        contract.setStartTime(vacancy.getStartTime());
        contract.setEndTime(vacancy.getEndTime());

        // Working Days - COPIAR la colección (importante)
        if (vacancy.getWorkingDays() != null) {
            List<String> workingDaysList = new ArrayList<>();
            for (EnumAll.WorkingDay day : vacancy.getWorkingDays()) {
                workingDaysList.add(day.toString());
            }
            contract.setWorkingDays(workingDaysList);
        } else {
            contract.setWorkingDays(new ArrayList<>());
        }

        // Beneficios - COPIAR la colección (importante)
        if (vacancy.getBenefits() != null) {
            contract.setBenefits(new ArrayList<>(vacancy.getBenefits()));
        } else {
            contract.setBenefits(new ArrayList<>());
        }

        contract.setVacationDays(22);
        contract.setVisaSponsorship(vacancy.getVisaSponsorship() != null ? vacancy.getVisaSponsorship() : false);

        // Condiciones laborales
        contract.setStartDate(LocalDate.now().plusDays(15));
        contract.setProbationPeriod("3 meses");
        contract.setTerminationNotice("15 días");

        // Aplicación
        contract.setApplicationId(application.getId());
        contract.setInterviewDate(application.getInterviewDate());
        contract.setContractGeneratedAt(LocalDateTime.now());

        // Timestamps
        contract.setCreatedAt(LocalDateTime.now());

        return contract;
    }

    private JobContractDTO convertToDTO(JobContractEntity entity) {
        return JobContractDTO.builder()
                .contractId(entity.getId())
                .contractNumber(entity.getContractNumber())
                .issuedAt(entity.getIssuedAt())
                .validUntil(entity.getValidUntil())
                .status(entity.getStatus())
                .contractUrl(entity.getContractUrl())
                .characterId(entity.getCharacterId())
                .characterName(entity.getCharacterName())
                .characterAge(entity.getCharacterAge())
                .characterBirthDate(entity.getCharacterBirthDate())
                .companyId(entity.getCompanyId())
                .companyName(entity.getCompanyName())
                .companyAddress(entity.getCompanyAddress())
                .companyPhone(entity.getCompanyPhone())
                .companyEmail(entity.getCompanyEmail())
                .companyWebsite(entity.getCompanyWebsite())
                .positionId(entity.getPositionId())
                .positionTitle(entity.getPositionTitle())
                .positionLevel(entity.getPositionLevel())
                .positionCategory(String.valueOf(entity.getPositionCategory()))
                .positionDescription(entity.getPositionDescription())
                .baseSalary(entity.getBaseSalary())
                .monthlySalary(entity.getMonthlySalary())
                .extraPayments(entity.getExtraPayments())
                .salaryCurrency(entity.getSalaryCurrency())
                .salaryPeriod(entity.getSalaryPeriod())
                .matchScore(entity.getMatchScore())
                .minSalaryRange(entity.getMinSalaryRange())
                .maxSalaryRange(entity.getMaxSalaryRange())
                .contractType(entity.getContractType())
                .workModality(entity.getWorkModality())
                .weeklyHours(entity.getWeeklyHours())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .workingDays(entity.getWorkingDays())
                .vacationDays(entity.getVacationDays())
                .benefits(entity.getBenefits())
                .visaSponsorship(entity.getVisaSponsorship())
                .startDate(entity.getStartDate())
                .probationPeriod(entity.getProbationPeriod())
                .terminationNotice(entity.getTerminationNotice())
                .applicationId(entity.getApplicationId())
                .interviewDate(entity.getInterviewDate())
                .contractGeneratedAt(entity.getContractGeneratedAt())
                .build();
    }

    private String generateContractNumber(JobApplicationEntity application, CompanyEntity company) {
        return String.format("CT-%d-%d-%d",
                company.getId(),
                application.getCharacterId(),
                System.currentTimeMillis());
    }

    // En JobApplicationServiceImpl
    @Override
    public Page<JobApplicationDTO> getByCharacterFilters(JobApplicationFiltersDTO filters, Pageable pageable) {
        log.info("Getting applications by character with filters: {}", filters);

        // Convertir listas vacías a null
        List<String> status = (filters.getStatus() == null || filters.getStatus().isEmpty()) ? null : filters.getStatus();
        List<String> stage = (filters.getStage() == null || filters.getStage().isEmpty()) ? null : filters.getStage();

        // Parsear fechas
        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        if (filters.getFromDate() != null && !filters.getFromDate().isEmpty()) {
            fromDate = LocalDateTime.parse(filters.getFromDate());
        }
        if (filters.getToDate() != null && !filters.getToDate().isEmpty()) {
            toDate = LocalDateTime.parse(filters.getToDate());
        }

        // Construir Specification dinámicamente
        Specification<JobApplicationEntity> spec = Specification
                .where(JobApplicationSpecifications.byCharacterId(filters.getCharacterId()))
                .and(JobApplicationSpecifications.bySearch(filters.getSearch()))
                .and(JobApplicationSpecifications.byStatus(status))
                .and(JobApplicationSpecifications.byStage(stage))
                .and(JobApplicationSpecifications.byMinMatchScore(filters.getMinMatchScore()))
                .and(JobApplicationSpecifications.byMaxMatchScore(filters.getMaxMatchScore()))
                .and(JobApplicationSpecifications.byFromDate(fromDate))
                .and(JobApplicationSpecifications.byToDate(toDate));

        Page<JobApplicationEntity> applicationsPage = jobApplicationRepository.findAll(spec, pageable);

        return applicationsPage.map(jobApplicationMapper::toDto);
    }

    // En JobApplicationServiceImpl
    @Override
    @Transactional
    public Map<String, Object> updateContractStatus(ContractActionDTO request) {
        log.info("Updating contract status: {}", request);

        JobContractEntity contract = jobContractRepository.findById(request.getContractId())
                .orElseThrow(() -> new RuntimeException("Contract not found: " + request.getContractId()));

        if (!"PENDING".equals(contract.getStatus())) {
            throw new RuntimeException("Contract cannot be modified. Current status: " + contract.getStatus());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("contractId", request.getContractId());
        result.put("accepted", request.isAccepted());
        result.put("notes", request.getNotes());

        if (request.isAccepted()) {
            // Aceptar contrato
            contract.setStatus("ACCEPTED");
            contract.setAcceptedAt(LocalDateTime.now());
            result.put("message", "Contrato aceptado correctamente");
            result.put("status", "ACCEPTED");
            CharacterJobDTO user = characterJobService.startJob(contract.getApplicationId());
            // Actualizar la aplicación original
            jobApplicationRepository.findById(contract.getApplicationId()).ifPresent(app -> {
                app.setStatus(EnumAll.ApplicationStatus.HIRED);
                app.setStage(EnumAll.ApplicationStage.HIRED);
                jobApplicationRepository.save(app);

                // Enviar notificación de éxito
                sendContractAcceptedNotification(contract);
            });

            log.info("Contract {} ACCEPTED", request.getContractId());
        } else {
            // Rechazar contrato
            contract.setStatus("REJECTED");
            contract.setRejectedAt(LocalDateTime.now());
            result.put("message", "Contrato rechazado");
            result.put("status", "REJECTED");

            // Actualizar la aplicación original
            jobApplicationRepository.findById(contract.getApplicationId()).ifPresent(app -> {
                app.setStatus(EnumAll.ApplicationStatus.REJECTED);
                app.setStage(EnumAll.ApplicationStage.APPLICATION);
                jobApplicationRepository.save(app);

                // Enviar notificación de rechazo
                sendContractRejectedNotification(app, contract);
            });

            log.info("Contract {} REJECTED", request.getContractId());
        }

        jobContractRepository.save(contract);

        return result;
    }

    private void sendContractAcceptedNotification(JobContractEntity contract) {
        CharacterDto characterDto = businessTransactions.getCharacter(contract.getCharacterId());

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserId(characterDto.getId());
        notificationDTO.setFromUserId(characterDto.getUid());
        notificationDTO.setType(NotificationType.JOBS);
        notificationDTO.setResourceType(NotificationResourceType.JOBS);
        notificationDTO.setResourceId(contract.getId());
        notificationDTO.setRead(false);
        notificationDTO.setActionUrl("/profile");
        notificationDTO.setTitle("✅ ¡Contrato aceptado!");
        notificationDTO.setSubTitle("Has aceptado la oferta laboral");
        notificationDTO.setMessage(String.format(
                "Has aceptado el contrato para el puesto de %s en %s con un salario de %s €. " +
                        "Tu fecha de inicio es el %s. ¡Enhorabuena y mucho éxito en tu nueva etapa!",
                contract.getPositionTitle(),
                contract.getCompanyName(),
                contract.getBaseSalary(),
                contract.getStartDate()
        ));
        notificationDTO.setEventType(NotificationEventType.JOB_OFFER);

        businessTransactions.setNotifications(notificationDTO);
    }

    private void sendContractRejectedNotification(JobApplicationEntity application, JobContractEntity contract) {
        CharacterDto characterDto = businessTransactions.getCharacter(application.getCharacterId());

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserId(characterDto.getId());
        notificationDTO.setFromUserId(characterDto.getUid());
        notificationDTO.setType(NotificationType.JOBS);
        notificationDTO.setResourceType(NotificationResourceType.JOBS);
        notificationDTO.setResourceId(contract.getId());
        notificationDTO.setRead(false);
        notificationDTO.setActionUrl("/profile");
        notificationDTO.setTitle("❌ Contrato rechazado");
        notificationDTO.setSubTitle("Has rechazado la oferta laboral");
        notificationDTO.setMessage(String.format(
                "Has rechazado el contrato para el puesto de %s en %s. " +
                        "Puedes seguir buscando otras oportunidades que se ajusten mejor a tus intereses.",
                contract.getPositionTitle(),
                contract.getCompanyName()
        ));
        notificationDTO.setEventType(NotificationEventType.JOB_APPLICATION_REJECTED);

        businessTransactions.setNotifications(notificationDTO);
    }

    private JobExperienceDto convertContractToJobExperience(JobContractEntity contract) {
        return JobExperienceDto.builder()
                .company(contract.getCompanyName())
                .role(contract.getPositionTitle())
                .startDate(contract.getStartDate() != null ? contract.getStartDate() : LocalDate.now().plusDays(15))
                .endDate(null) // Trabajo actual, sin fecha de fin
                .monthlySalary(contract.getMonthlySalary())
                .performanceScore(0) // Inicialmente 0, se irá actualizando
                .responsibilities(new ArrayList<>())
                .skillsGained(new ArrayList<>())
                .contractType(contract.getContractType())
                .workModality(contract.getWorkModality())
                .weeklyHours(contract.getWeeklyHours())
                .startTime(contract.getStartTime() != null ? contract.getStartTime().toString() : null)
                .endTime(contract.getEndTime() != null ? contract.getEndTime().toString() : null)
                .applicationId(contract.getApplicationId())
                .vacancyId(contract.getPositionId())
                .build();
    }



    // En JobApplicationServiceImpl
    @Override
    public Page<JobContractDTO> getCharacterContractsByFilters(JobContractFiltersDTO filters, Pageable pageable) {
        log.info("Getting contracts by filters using Specifications: {}", filters);

        // Convertir fechas
        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        if (filters.getFromDate() != null && !filters.getFromDate().isEmpty()) {
            fromDate = LocalDateTime.parse(filters.getFromDate());
        }
        if (filters.getToDate() != null && !filters.getToDate().isEmpty()) {
            toDate = LocalDateTime.parse(filters.getToDate());
        }

        // Construir Specification dinámicamente
        Specification<JobContractEntity> spec = Specification
                .where(JobContractSpecifications.byCharacterId(filters.getCharacterId()))
                .and(JobContractSpecifications.bySearch(filters.getSearch()))
                .and(JobContractSpecifications.byStatus(filters.getStatus()))
                .and(JobContractSpecifications.byMinSalary(filters.getMinSalary()))
                .and(JobContractSpecifications.byMaxSalary(filters.getMaxSalary()))
                .and(JobContractSpecifications.byContractType(filters.getContractType()))
                .and(JobContractSpecifications.byWorkModality(filters.getWorkModality()))
                .and(JobContractSpecifications.byFromDate(fromDate))
                .and(JobContractSpecifications.byToDate(toDate));

        Page<JobContractEntity> contractsPage = jobContractRepository.findAll(spec, pageable);

        return contractsPage.map(this::convertToDTO);
    }
}