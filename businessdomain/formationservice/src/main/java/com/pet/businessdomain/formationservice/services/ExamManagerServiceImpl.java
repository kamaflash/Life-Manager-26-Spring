package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.shareddto.dto.FormationExamDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ExamManagerServiceImpl implements ExamManagerService {

    private final ICharacterTrainingService characterTrainingService;
    private final FormationExamService formationExamService;
    private final CharacterExamService characterExamService;

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

        // 5️⃣ Simular el examen (tipo RPG: score aleatorio influido por dificultad)
        int maxScore = exam.getMaxScore() != null ? exam.getMaxScore() : 10;
        int difficultyModifier = switch (exam.getDifficulty()) {
            case BASIC -> 2;
            case INTERMEDIATE -> 0;
            case ADVANCED -> -2;
            case EXPERT -> 4;
        };
        int score = Math.min(maxScore, Math.max(0,
                random.nextInt(maxScore + 1) + difficultyModifier
        ));

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

        // 9️⃣ Actualizar CharacterTraining si pasó
        if (status == EnumAll.ExamStatus.PASSED) {
            training.setStatus(EnumAll.TrainingStatus.COMPLETED);
            training.setFinishedAt(LocalDateTime.now());
            characterTrainingService.save(training);
        }

        return savedExam;
    }
}
