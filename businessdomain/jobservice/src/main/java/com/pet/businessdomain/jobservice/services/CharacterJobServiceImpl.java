package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.CharacterJobEntity;
import com.pet.businessdomain.jobservice.entities.JobApplicationEntity;
import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.jobservice.mapper.CharacterJobMapper;
import com.pet.businessdomain.jobservice.repository.CharacterJobRepository;
import com.pet.businessdomain.jobservice.repository.JobApplicationRepository;
import com.pet.businessdomain.jobservice.repository.JobVacancyRepository;
import com.pet.businessdomain.jobservice.services.CharacterJobService;
import com.pet.businessdomain.shareddto.dto.CharacterJobDTO;
import com.pet.businessdomain.shareddto.dto.WorkProgressDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterJobServiceImpl implements CharacterJobService {

    private final CharacterJobRepository characterJobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobVacancyRepository jobVacancyRepository;
    private final CharacterJobMapper characterJobMapper;

    @Override
    @Transactional
    public CharacterJobDTO startJob(Long applicationId) {
        log.info("Starting job from application: {}", applicationId);

        JobApplicationEntity application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        if (!EnumAll.ApplicationStatus.OFFERED.equals(application.getStatus())) {
            throw new RuntimeException("Cannot start job: application not in OFFERED status");
        }

        CharacterJobEntity entity = new CharacterJobEntity();
        entity.setCharacterId(application.getCharacterId());
        entity.setVacancy(application.getVacancy());
        entity.setStartDate(LocalDate.now());

        CharacterJobEntity saved = characterJobRepository.save(entity);

        // Actualizar estado de la aplicación
        application.setStatus(EnumAll.ApplicationStatus.HIRED);
        application.setStage(EnumAll.ApplicationStage.HIRED);
        jobApplicationRepository.save(application);

        return characterJobMapper.toDto(saved);
    }

    @Override
    public CharacterJobDTO getById(Long id) {
        return characterJobRepository.findById(id)
                .map(characterJobMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Character job not found with id: " + id));
    }

    @Override
    public CharacterJobDTO getCurrentJob(Long characterId) {
        return characterJobRepository.findFirstByCharacterIdAndActiveTrueOrderByStartDateDesc(characterId)
                .map(characterJobMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public void resign(Long id) {
        log.info("Resigning from job: {}", id);
        characterJobRepository.terminateJob(id, LocalDate.now());
    }

    @Override
    @Transactional
    public void terminate(Long id, String reason) {
        log.info("Terminating job {}: {}", id, reason);
        characterJobRepository.terminateJob(id, LocalDate.now());
    }

    @Override
    public List<CharacterJobDTO> getByCharacter(Long characterId) {
        List<CharacterJobEntity> characterJobEntities = characterJobRepository.findByCharacterId(characterId);
        return characterJobMapper.toDtoList(characterJobRepository.findByCharacterId(characterId));
    }

    @Override
    public List<CharacterJobDTO> getActiveByCharacter(Long characterId) {
        return characterJobMapper.toDtoList(characterJobRepository.findByCharacterIdAndActiveTrue(characterId));
    }

    @Override
    public List<CharacterJobDTO> getHistoryByCharacter(Long characterId) {
        return characterJobMapper.toDtoList(characterJobRepository.findByCharacterId(characterId));
    }

    @Override
    public List<CharacterJobDTO> getByCompany(Long companyId) {
        return characterJobMapper.toDtoList(characterJobRepository.findByCompanyId(companyId));
    }

    @Override
    @Transactional
    public WorkProgressDTO workDay(Long characterJobId, Integer hoursWorked) {
        log.info("Working day for job {}: {} hours", characterJobId, hoursWorked);

        CharacterJobEntity job = characterJobRepository.findById(characterJobId)
                .orElseThrow(() -> new RuntimeException("Character job not found"));

        WorkProgressDTO progress = new WorkProgressDTO();
        progress.setCharacterJobId(characterJobId);
        progress.setHoursWorked(hoursWorked);

        // Calcular cambios
        int productivityGain = hoursWorked / 4; // 1 punto por cada 4 horas
        int stressGain = hoursWorked / 2; // 1 punto por cada 2 horas
        int satisfactionChange = hoursWorked > 8 ? -2 : 1;

        // Actualizar estadísticas
        job.setPerformance(Math.min(100, job.getPerformance() + productivityGain));
        job.setStressLevel(Math.min(100, job.getStressLevel() + stressGain));
        job.setSatisfaction(Math.min(100, Math.max(0, job.getSatisfaction() + satisfactionChange)));

        characterJobRepository.save(job);

        progress.setProductivityGain(productivityGain);
        progress.setStressGain(stressGain);
        progress.setSatisfactionChange(satisfactionChange);

        // Calcular salario ganado
        JobVacancyEntity vacancy = job.getVacancy();
        if (vacancy.getMinSalary() != null) {
            BigDecimal dailySalary = vacancy.getMinSalary().divide(BigDecimal.valueOf(22)); // 22 días laborables
            BigDecimal earned = dailySalary.multiply(BigDecimal.valueOf(hoursWorked / 8));
            progress.setSalaryEarned(earned.intValue());
        }

        return progress;
    }

    @Override
    @Transactional
    public WorkProgressDTO studyOnJob(Long characterJobId) {
        log.info("Studying on job for: {}", characterJobId);

        WorkProgressDTO progress = new WorkProgressDTO();
        progress.setCharacterJobId(characterJobId);

        CharacterJobEntity job = characterJobRepository.findById(characterJobId)
                .orElseThrow(() -> new RuntimeException("Character job not found"));

        // Estudiar mejora productividad pero aumenta estrés
        job.setPerformance(Math.min(100, job.getPerformance() + 3));
        job.setStressLevel(Math.min(100, job.getStressLevel() + 2));

        characterJobRepository.save(job);

        progress.setProductivityGain(3);
        progress.setStressGain(2);

        return progress;
    }

    @Override
    @Transactional
    public WorkProgressDTO requestPromotion(Long characterJobId) {
        log.info("Requesting promotion for job: {}", characterJobId);

        WorkProgressDTO progress = new WorkProgressDTO();
        progress.setCharacterJobId(characterJobId);

        CharacterJobEntity job = characterJobRepository.findById(characterJobId)
                .orElseThrow(() -> new RuntimeException("Character job not found"));

        if (!canRequestPromotion(characterJobId)) {
            progress.setProductivityGain(0);
            progress.setStressGain(5);
            progress.setSatisfactionChange(-5);
            return progress;
        }

        // 50% de probabilidad de éxito
        boolean success = Math.random() > 0.5;

        if (success) {
            characterJobRepository.incrementPromotions(characterJobId);
            job.setSatisfaction(Math.min(100, job.getSatisfaction() + 10));
            progress.setSatisfactionChange(10);
            progress.setStressGain(-5);
        } else {
            job.setSatisfaction(Math.max(0, job.getSatisfaction() - 10));
            job.setStressLevel(Math.min(100, job.getStressLevel() + 10));
            progress.setSatisfactionChange(-10);
            progress.setStressGain(10);
        }

        characterJobRepository.save(job);

        return progress;
    }

    @Override
    @Transactional
    public WorkProgressDTO requestRaise(Long characterJobId) {
        log.info("Requesting raise for job: {}", characterJobId);

        WorkProgressDTO progress = new WorkProgressDTO();
        progress.setCharacterJobId(characterJobId);

        CharacterJobEntity job = characterJobRepository.findById(characterJobId)
                .orElseThrow(() -> new RuntimeException("Character job not found"));

        // 40% de probabilidad de éxito
        boolean success = Math.random() > 0.6;

        if (success) {
            job.setBonusesReceived(job.getBonusesReceived() + 1);
            job.setSatisfaction(Math.min(100, job.getSatisfaction() + 5));
            progress.setSatisfactionChange(5);
        } else {
            job.setSatisfaction(Math.max(0, job.getSatisfaction() - 5));
            job.setStressLevel(Math.min(100, job.getStressLevel() + 5));
            progress.setSatisfactionChange(-5);
            progress.setStressGain(5);
        }

        characterJobRepository.save(job);

        return progress;
    }

    @Override
    @Transactional
    public void updatePerformance(Long id, Integer performance) {
        characterJobRepository.updatePerformance(id, performance);
    }

    @Override
    @Transactional
    public void updateSatisfaction(Long id, Integer satisfaction) {
        characterJobRepository.updateSatisfaction(id, satisfaction);
    }

    @Override
    @Transactional
    public void updateStress(Long id, Integer stressChange) {
        CharacterJobEntity job = characterJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character job not found"));
        job.setStressLevel(Math.min(100, Math.max(0, job.getStressLevel() + stressChange)));
        characterJobRepository.save(job);
    }

    @Override
    @Transactional
    public void addPromotion(Long id) {
        characterJobRepository.incrementPromotions(id);
    }

    @Override
    @Transactional
    public void addBonus(Long id, Integer amount) {
        CharacterJobEntity job = characterJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character job not found"));
        job.setBonusesReceived(job.getBonusesReceived() + 1);
        characterJobRepository.save(job);
    }

    @Override
    public boolean hasActiveJob(Long characterId) {
        return characterJobRepository.existsByCharacterIdAndActiveTrue(characterId);
    }

    @Override
    public boolean canWorkToday(Long characterJobId) {
        // TODO: Implementar lógica de verificación de días trabajados
        return true;
    }

    @Override
    public boolean canRequestPromotion(Long characterJobId) {
        CharacterJobEntity job = characterJobRepository.findById(characterJobId)
                .orElse(null);
        if (job == null) return false;

        // Al menos 6 meses en el puesto y rendimiento > 70
        long monthsEmployed = java.time.temporal.ChronoUnit.MONTHS.between(
                job.getStartDate(), LocalDate.now());

        return monthsEmployed >= 6 && job.getPerformance() >= 70;
    }
}