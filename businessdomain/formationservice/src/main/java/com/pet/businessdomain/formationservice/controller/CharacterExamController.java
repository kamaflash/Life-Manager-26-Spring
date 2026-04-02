package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.formationservice.services.CharacterExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/character-exams")
@RequiredArgsConstructor
public class CharacterExamController {

    @Autowired
    private final CharacterExamService characterExamService;

    // =========================
    // 🔹 Exámenes de un personaje
    // =========================
    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<CharacterExam>> getByCharacter(@PathVariable Long characterId) {
        List<CharacterExam> exams = characterExamService.getByCharacterId(characterId);

        if (exams.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(exams);
    }

    // =========================
    // 🔹 Exámenes de un training
    // =========================
    @GetMapping("/training/{trainingId}")
    public ResponseEntity<List<CharacterExam>> getByTraining(@PathVariable Long trainingId) {
        List<CharacterExam> exams = characterExamService.getByCharacterTrainingId(trainingId);

        if (exams.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(exams);
    }

    // =========================
    // 🔹 Último intento
    // =========================
    @GetMapping("/training/{trainingId}/last")
    public ResponseEntity<?> getLastAttempt(@PathVariable Long trainingId) {
        return characterExamService.getLastAttempt(trainingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
