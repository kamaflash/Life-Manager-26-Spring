package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.formationservice.services.FormationExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formation-exams")
@RequiredArgsConstructor
public class FormationExamController {
    @Autowired
    private final FormationExamService formationExamService;

    // =========================
    // 🔹 Obtener exámenes por formación
    // =========================
    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<FormationExam>> getByFormation(@PathVariable(name = "formationId") Long formationId) {
        List<FormationExam> exams = formationExamService.getByFormationId(formationId);

        if (exams.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(exams);
    }

    // =========================
    // 🔹 Obtener examen por ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<FormationExam> getById(@PathVariable(name = "id") Long id) {
        FormationExam exam = formationExamService.getById(id);

        if (exam == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(exam);
    }

    // =========================
    // 🔹 Crear examen
    // =========================
    @PostMapping
    public ResponseEntity<FormationExam> create(@RequestBody FormationExam exam) {
        FormationExam saved = formationExamService.save(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // =========================
    // 🔹 Desactivar examen
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") Long id) {
        FormationExam exam = formationExamService.getById(id);

        if (exam == null) return ResponseEntity.notFound().build();

        exam.setActive(false);
        formationExamService.save(exam);

        return ResponseEntity.noContent().build();
    }
}
