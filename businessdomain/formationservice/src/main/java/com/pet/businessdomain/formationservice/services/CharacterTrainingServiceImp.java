package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.formationservice.repository.FormationRepository;
import com.pet.businessdomain.formationservice.repository.ICharacterTrainingRepository;
import com.pet.businessdomain.formationservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.CharacterTrainingDto;
import com.pet.businessdomain.shareddto.dto.SExpenseResponseDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CharacterTrainingServiceImp implements ICharacterTrainingService{
    @Autowired
    private ICharacterTrainingRepository trainingRepo;

    @Autowired
    private FormationRepository formationRepo;

    @Autowired
    private BusinessTransactions businessTransactions;
    // Todos los cursos del personaje
    public List<CharacterTraining> getTrainingsForCharacter(Long characterId) {
        return trainingRepo.findByCharacterId(characterId);
    }

    // Cursos completados
    public List<CharacterTraining> getCompletedTrainings(Long characterId) {
        return trainingRepo.findByCharacterId(characterId);
    }

    // Cursos disponibles (puede incluir lógicos según XP y nivel)
    public List<Formation> getAvailableFormations(Long characterId, EnumAll.EducationLevel eduLevel, int academicXp, int academicLevel, EnumAll.CareerInterest career) {
        return formationRepo.findAvailableFormations(eduLevel, academicLevel, academicXp, career);
    }

    public CharacterTrainingDto subscribeToCourse(CharacterTrainingDto dto, Long id) throws BusinessRuleException {
        dto.setCharacterId(id);
        // Verificar si ya está inscrito
        boolean exists = trainingRepo.existsByCharacterIdAndTrainingId(dto.getCharacterId(), dto.getTrainingId());
        if (exists) {
            throw new BusinessRuleException(
                    "1001",                               // código de error
                    "Ya estas matriculado",
                    HttpStatus.BAD_REQUEST
            );
        }

        // Buscar la formación
        Formation formation = formationRepo.findById(dto.getTrainingId())
                .orElseThrow(() -> new BusinessRuleException(
                        "1001",                               // código de error
                        "El código de la formación es obligatorio",
                        HttpStatus.BAD_REQUEST
                ));

        // Crear la entidad CharacterTraining
        CharacterTraining training = new CharacterTraining();
        training.setCharacterId(dto.getCharacterId());
        training.setTrainingId(dto.getTrainingId());
        training.setTrainingName(dto.getTrainingName());
        training.setTrainingType(dto.getTrainingType());
        training.setTrainingDifficulty(dto.getTrainingDifficulty());
        training.setStatus(EnumAll.TrainingStatus.AVAILABLE);
        training.setProgress(0);
        training.setInvestedHours(0);
        training.setStartedAt(LocalDateTime.now());
        training.setFinishedAt(null);
        training.setAcademicXpGained(dto.getAcademicXpGained());
        training.setApplied(false);

        trainingRepo.save(training);

        // Mapear al DTO incluyendo info opcional
        dto.setId(training.getId());
        dto.setStatus(training.getStatus());
        dto.setProgress(training.getProgress());
        dto.setInvestedHours(training.getInvestedHours());
        dto.setStartedAt(training.getStartedAt());
        dto.setFinishedAt(training.getFinishedAt());
        dto.setAcademicXpGained(training.getAcademicXpGained());
        dto.setApplied(training.getApplied());

        dto.setTrainingName(formation.getName());
        dto.setTrainingType(formation.getType());
        dto.setTrainingDifficulty(formation.getDifficulty());
        SExpenseResponseDto expenseResponseDto = new SExpenseResponseDto();
        expenseResponseDto.setCategory(EnumAll.ExpenseCategory.EDUCATION);
        expenseResponseDto.setAmount(formation.getCost());
        expenseResponseDto.setConcept(formation.getName());
        expenseResponseDto.setExternalRefId(training.getCharacterId());
        expenseResponseDto.setStartDate(training.getStartedAt().toLocalDate());
        expenseResponseDto.setFrequency(EnumAll.Frequency.YEARLY);
        SExpenseResponseDto sExpenseResponseDto = businessTransactions.setExpense(expenseResponseDto,dto.getCharacterId());
        return dto;
    }

    @Override
    public List<Formation> getAvailableCoursesForCharacter(Long characterId) {

        CharacterDto personDto = businessTransactions.getPerson(characterId);

        List<Formation> allTrainings = formationRepo.findAllByActiveTrue();

        List<CharacterTraining> listTraining =
                trainingRepo.findByCharacterId(personDto.getId());

        Set<Long> completedFormationIds = listTraining.stream()
                .map(CharacterTraining::getTrainingId)
                .collect(Collectors.toSet());

        return allTrainings.stream()
                .filter(training ->
                        !completedFormationIds.contains(training.getId())
                                && training.getCategory() != null
                                && personDto.getInterests() != null
                                && training.getCategory() ==
                                EnumAll.CareerInterest.valueOf(personDto.getInterests().getFirst())
                                && personDto.getXpAcademy() != null
                                && training.getMinAcademicXp() != null
                                && training.getMaxAcademicXp() != null
                                && personDto.getXpAcademy() >= training.getMinAcademicXp()
                                && personDto.getXpAcademy() < training.getMaxAcademicXp()
                )
                .toList();
    }
}
