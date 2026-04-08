package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.*;
import com.pet.businessdomain.formationservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.NotificationResourceType;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ExamManagerServiceImpl implements ExamManagerService {

    @Autowired
    private ICharacterTrainingService characterTrainingService;
    @Autowired
    private FormationExamService formationExamService;

    @Autowired
    private FormationService formationService;
    @Autowired
    private CharacterExamService characterExamService;
    @Autowired
    private BusinessTransactions businessTransactions;

    private final Random random = new Random();

    @Override
    public CharacterExam takeExam(Long characterId, Long trainingId) {

        // 1️⃣ Obtener CharacterTraining
        CharacterTraining training = characterTrainingService.getById(trainingId);
        if (training == null || !training.getCharacterId().equals(characterId)) {
            throw new IllegalStateException("Training no encontrado o no pertenece al personaje");
        }

        // 2️⃣ Obtener el examen correspondiente según horas invertidas
        FormationExamDto exam = formationExamService.getByFormationId(training.getTrainingId())
                .stream()
                .filter(e -> e.getRequiredHours() <= training.getInvestedHours())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay examen disponible aún"));

        // 3️⃣ Validar intentos
        int attempts = characterExamService.countAttempts(trainingId, exam.getId());
        if (exam.getMaxAttempts() != null && attempts >= exam.getMaxAttempts()) {
            throw new IllegalStateException("Número máximo de intentos alcanzado");
        }

        // 4️⃣ Validar si ya aprobó
        boolean passed = characterExamService.existsPassed(trainingId);
        if (passed && exam.getMandatory()) {
            throw new IllegalStateException("Examen ya aprobado");
        }

        // 5️⃣ Calcular score basado en horas de estudio
        int maxScore = exam.getMaxScore() != null ? exam.getMaxScore() : 10;
        double studyRatio = training.getStudyHours() != null && training.getStudyHours() > 0 
            ? (double) training.getStudyHours() / exam.getRequiredHours() 
            : 0.0;
        int baseScore = (int) (studyRatio * maxScore);
        // Si estudio escaso (< 50%), probabilidad de suspender
        if (studyRatio < 0.5) {
            baseScore = Math.max(0, baseScore - random.nextInt(3)); // penalización
        }
        int score = Math.min(maxScore, Math.max(0, baseScore + random.nextInt(3) - 1)); // pequeña variación

        // 6️⃣ Determinar resultado
        EnumAll.ExamStatus status = score >= exam.getMinPassingScore() ? EnumAll.ExamStatus.PASSED : EnumAll.ExamStatus.FAILED;

        // 7️⃣ Crear CharacterExam
        CharacterExam characterExam = new CharacterExam();
        characterExam.setCharacterId(characterId);
        characterExam.setCharacterTrainingId(trainingId);
        characterExam.setFormationExamId(exam.getId());
        characterExam.setExamDate(LocalDateTime.now());
        characterExam.setAttemptNumber(attempts + 1);
        characterExam.setScore(score);
        characterExam.setStatus(status);

        // 8️⃣ Guardar examen
        CharacterExam savedExam = characterExamService.save(characterExam);

        // 9️⃣ Actualizar CharacterTraining
        training.setGrade((double) score); // actualizar nota
        if (isFinalExam(training.getTrainingId(), exam) && status == EnumAll.ExamStatus.PASSED) {
            training.setStatus(EnumAll.TrainingStatus.COMPLETED);
            training.setFinishedAt(LocalDateTime.now());
            CharacterDto characterDto = businessTransactions.getPerson(training.getCharacterId());
            Formation formation = formationService.getById(training.getTrainingId());
            Map<String, Integer> rewards = formation.getStatRewards();
            CharacterStats characterStats = mapToCharacterStats(rewards);

            applyStats(characterDto, characterStats);
            setNotification(characterDto, training);
        }
        characterTrainingService.save(training);

        return savedExam;
    }

    private boolean isFinalExam(Long formationId, FormationExamDto exam) {
        List<FormationExamDto> exams = formationExamService.getByFormationId(formationId);
        int maxHours = exams.stream().mapToInt(FormationExamDto::getRequiredHours).max().orElse(0);
        return exam.getRequiredHours() == maxHours;
    }

    private void applyStats(CharacterDto characterDto, CharacterStats inc) {

        if (characterDto.getStats() == null) {
            characterDto.setStats(new StatsDto());
        }

        StatsDto base = characterDto.getStats();

        // INTELLIGENCE
        if (inc.getIntelligence() != null) {
            base.setIntelligence(base.getIntelligence() + inc.getIntelligence());
        }

        // CHARISMA
        if (inc.getCharisma() != null) {
            base.setCharisma(base.getCharisma() + inc.getCharisma());
        }

        // CREATIVITY
        if (inc.getCreativity() != null) {
            base.setCreativity(base.getCreativity() + inc.getCreativity());
        }

        // RESILIENCE
        if (inc.getResilience() != null) {
            base.setResilience(base.getResilience() + inc.getResilience());
        }

        // HEALTH (0–100)
        if (inc.getHealth() != null) {
            base.setHealth(clamp(base.getHealth() + inc.getHealth(), 0, 100));
        }

        // ENERGY (0–100)
        if (inc.getEnergy() != null) {
            base.setEnergy(clamp(base.getEnergy() + inc.getEnergy(), 0, 100));
        }

        // HAPPINESS (0–100)
        if (inc.getHappiness() != null) {
            base.setHappiness(clamp(base.getHappiness() + inc.getHappiness(), 0, 100));
        }

        // STRESS (0–100 pero normalmente baja)
        if (inc.getStress() != null) {
            base.setStress(clamp(base.getStress() + inc.getStress(), 0, 100));
        }

        // FINANCES (sin límite o ajusta si quieres)
        if (inc.getFinances() != null) {
            base.setFinances(base.getFinances() + inc.getFinances());
        }

        characterDto.setStats(base);

        characterDto = businessTransactions.updatePerson(characterDto);

    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    private CharacterStats mapToCharacterStats(Map<String, Integer> map) {
        CharacterStats stats = new CharacterStats();

        stats.setIntelligence(map.get("intelligence"));
        stats.setCharisma(map.get("charisma"));
        stats.setCreativity(map.get("creativity"));
        stats.setResilience(map.get("resilience"));
        stats.setHealth(map.get("health"));
        stats.setEnergy(map.get("energy"));
        stats.setHappiness(map.get("happiness"));
        stats.setStress(map.get("stress"));
        stats.setFinances(map.get("finances"));

        return stats;
    }

    private void setNotification(CharacterDto dto, CharacterTraining training) {

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setUserId(dto.getId());
        notificationDTO.setFromUserId(dto.getUid());
        notificationDTO.setType(NotificationType.SYSTEM);

        notificationDTO.setTitle("¡Examen aprobado!");
        notificationDTO.setSubTitle("Has aprobado el examen final sobre "+training.getTrainingName());
        notificationDTO.setMessage("Curso completado. La nota final es "+training.getGrade()+"/100.");

        // 🔥 Nuevo sistema
        notificationDTO.setResourceType(NotificationResourceType.COURSE);
        notificationDTO.setResourceId(dto.getId());

        // 🔥 Navegación directa frontend
        notificationDTO.setActionUrl("/profile" );

        // 🔥 Metadata (opcional pero muy recomendable)
        notificationDTO.setMetadata("""
        {
            "characterId": %d
        }
    """.formatted(dto.getId()));

        notificationDTO.setRead(false);

        businessTransactions.setNotifications(notificationDTO);
    }
}
