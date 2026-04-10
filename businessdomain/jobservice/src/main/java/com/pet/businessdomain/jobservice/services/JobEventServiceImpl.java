package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.CharacterJobEntity;
import com.pet.businessdomain.jobservice.entities.JobEventEntity;
import com.pet.businessdomain.jobservice.mapper.JobEventMapper;
import com.pet.businessdomain.jobservice.repository.CharacterJobRepository;
import com.pet.businessdomain.jobservice.repository.JobEventRepository;
import com.pet.businessdomain.shareddto.dto.JobEventDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobEventServiceImpl implements JobEventService {

    private final JobEventRepository jobEventRepository;
    private final CharacterJobRepository characterJobRepository;
    private final JobEventMapper jobEventMapper;
    private final Random random = new Random();

    @Override
    public JobEventDTO getById(Long id) {
        return jobEventRepository.findById(id)
                .map(jobEventMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }
    @Override
    @Transactional
    public JobEventDTO create(JobEventDTO dto) {
        log.info("Creating new job event: {}", dto.getTitle());

        JobEventEntity entity = jobEventMapper.toEntity(dto);
        JobEventEntity saved = jobEventRepository.save(entity);
        return jobEventMapper.toDto(saved);
    }
    @Override
    public List<JobEventDTO> getByCharacterJob(Long characterJobId) {
        return jobEventMapper.toDtoList(jobEventRepository.findByCharacterJobId(characterJobId));
    }

    @Override
    @Transactional
    public JobEventDTO generateRandomEvent(Long characterJobId) {
        log.info("Generating random event for job {}", characterJobId);

        String[] eventTypes = {"BONUS", "CONFLICT", "PROJECT_SUCCESS", "PROJECT_FAILURE"};
        String type = eventTypes[random.nextInt(eventTypes.length)];

        switch (type) {
            case "BONUS":
                return generateBonusEvent(characterJobId, 100 + random.nextInt(400));
            case "CONFLICT":
                return generateConflictEvent(characterJobId, null);
            case "PROJECT_SUCCESS":
                return generateProjectSuccessEvent(characterJobId, 10 + random.nextInt(40));
            case "PROJECT_FAILURE":
                return generateProjectFailureEvent(characterJobId);
            default:
                return null;
        }
    }

    @Override
    @Transactional
    public JobEventDTO generateBonusEvent(Long characterJobId, Integer amount) {
        log.info("Generating bonus event for job {}: {} €", characterJobId, amount);

        JobEventEntity event = new JobEventEntity();
        event.setCharacterJobId(characterJobId);
        event.setType(EnumAll.EventType.BONUS);  // ✅ CORREGIDO: setEventType → setType
        event.setTitle("¡Bono recibido!");
        event.setDescription("Has recibido un bono de " + amount + " € por tu buen desempeño");
        event.setSalaryChange(amount);
        event.setPerformanceChange(5);
        event.setSatisfactionChange(10);
        event.setStressChange(-5);

        JobEventEntity saved = jobEventRepository.save(event);
        applyEventEffects(characterJobId, jobEventMapper.toDto(saved));

        return jobEventMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobEventDTO generatePromotionEvent(Long characterJobId) {
        log.info("Generating promotion event for job {}", characterJobId);

        JobEventEntity event = new JobEventEntity();
        event.setCharacterJobId(characterJobId);
        event.setType(EnumAll.EventType.PROMOTION);  // ✅ CORREGIDO: setEventType → setType
        event.setTitle("¡Ascenso!");
        event.setDescription("¡Felicidades! Has sido ascendido");
        event.setSalaryChange(200);
        event.setPerformanceChange(10);
        event.setSatisfactionChange(20);
        event.setStressChange(10);

        JobEventEntity saved = jobEventRepository.save(event);
        applyEventEffects(characterJobId, jobEventMapper.toDto(saved));

        // Incrementar promociones en el trabajo
        characterJobRepository.incrementPromotions(characterJobId);

        return jobEventMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobEventDTO generateConflictEvent(Long characterJobId, Long withCharacterId) {
        log.info("Generating conflict event for job {}", characterJobId);

        JobEventEntity event = new JobEventEntity();
        event.setCharacterJobId(characterJobId);
        event.setType(EnumAll.EventType.CONFLICT);  // ✅ CORREGIDO: setEventType → setType
        event.setTitle("Conflicto laboral");
        event.setDescription("Has tenido un desacuerdo con un compañero");
        event.setPerformanceChange(-5);
        event.setSatisfactionChange(-10);
        event.setStressChange(15);

        JobEventEntity saved = jobEventRepository.save(event);
        applyEventEffects(characterJobId, jobEventMapper.toDto(saved));

        return jobEventMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobEventDTO generateProjectSuccessEvent(Long characterJobId, Integer xpReward) {
        log.info("Generating project success event for job {}", characterJobId);

        JobEventEntity event = new JobEventEntity();
        event.setCharacterJobId(characterJobId);
        event.setType(EnumAll.EventType.PROJECT_SUCCESS);  // ✅ CORREGIDO: setEventType → setType
        event.setTitle("Proyecto exitoso");
        event.setDescription("Tu proyecto ha sido un éxito");
        event.setPerformanceChange(10);
        event.setSatisfactionChange(15);
        event.setStressChange(-10);

        JobEventEntity saved = jobEventRepository.save(event);
        applyEventEffects(characterJobId, jobEventMapper.toDto(saved));

        return jobEventMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobEventDTO generateProjectFailureEvent(Long characterJobId) {
        log.info("Generating project failure event for job {}", characterJobId);

        JobEventEntity event = new JobEventEntity();
        event.setCharacterJobId(characterJobId);
        event.setType(EnumAll.EventType.PROJECT_FAILURE);  // ✅ CORREGIDO: setEventType → setType
        event.setTitle("Proyecto fallido");
        event.setDescription("Tu proyecto no ha salido como esperabas");
        event.setPerformanceChange(-10);
        event.setSatisfactionChange(-15);
        event.setStressChange(15);

        JobEventEntity saved = jobEventRepository.save(event);
        applyEventEffects(characterJobId, jobEventMapper.toDto(saved));

        return jobEventMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobEventDTO generateBurnoutEvent(Long characterJobId) {
        log.info("Generating burnout event for job {}", characterJobId);

        JobEventEntity event = new JobEventEntity();
        event.setCharacterJobId(characterJobId);
        event.setType(EnumAll.EventType.BURNOUT);  // ✅ CORREGIDO: setEventType → setType
        event.setTitle("Agotamiento");
        event.setDescription("Estás sufriendo agotamiento laboral");
        event.setPerformanceChange(-20);
        event.setSatisfactionChange(-25);
        event.setStressChange(30);

        JobEventEntity saved = jobEventRepository.save(event);
        applyEventEffects(characterJobId, jobEventMapper.toDto(saved));

        return jobEventMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void resolveEvent(Long eventId) {
        log.info("Resolving event {}", eventId);
        JobEventEntity event = jobEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setResolved(true);
        jobEventRepository.save(event);
    }

    @Override
    @Transactional
    public void applyEventEffects(Long characterJobId, JobEventDTO event) {
        CharacterJobEntity job = characterJobRepository.findById(characterJobId)
                .orElseThrow(() -> new RuntimeException("Character job not found"));

        if (event.getPerformanceChange() != null) {
            job.setPerformance(Math.min(100, Math.max(0,
                    job.getPerformance() + event.getPerformanceChange())));
        }
        if (event.getSatisfactionChange() != null) {
            job.setSatisfaction(Math.min(100, Math.max(0,
                    job.getSatisfaction() + event.getSatisfactionChange())));
        }
        if (event.getStressChange() != null) {
            job.setStressLevel(Math.min(100, Math.max(0,
                    job.getStressLevel() + event.getStressChange())));
        }

        characterJobRepository.save(job);
    }

    @Override
    public List<JobEventDTO> getPositiveEvents(Long characterJobId) {
        return jobEventMapper.toDtoList(jobEventRepository.findPositiveEvents(characterJobId));
    }

    @Override
    public List<JobEventDTO> getNegativeEvents(Long characterJobId) {
        return jobEventMapper.toDtoList(jobEventRepository.findNegativeEvents(characterJobId));
    }

    @Override
    public List<JobEventDTO> getRecentEvents(Long characterJobId, int limit) {
        return jobEventMapper.toDtoList(
                jobEventRepository.findTop5ByCharacterJobIdOrderByOccurredAtDesc(characterJobId)
        );
    }

    @Override
    public Long countEventsByType(Long characterJobId, String eventType) {
        List<Object[]> results = jobEventRepository.countEventsByType(characterJobId);
        return results.stream()
                .filter(r -> r[0].equals(eventType))
                .map(r -> ((Number) r[1]).longValue())
                .findFirst()
                .orElse(0L);
    }
}