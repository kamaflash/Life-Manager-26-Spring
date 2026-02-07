/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pet.businessdomain.formationservice.repository;

import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.entities.enumentities.Enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FormationRepository extends JpaRepository<Formation, Long> {

    // ===== BÁSICOS =====

    Optional<Formation> findByCode(String code);

    List<Formation> findByActiveTrue();
    boolean existsByCode(String code);
    Page<Formation> findByActiveTrue(Pageable pageable);

    // ===== FILTROS SIMPLES =====

    List<Formation> findByCategoryAndActiveTrue(Enum.CareerInterest category);

    List<Formation> findByDifficultyAndActiveTrue(Enum.DifficultyLevel difficulty);

    List<Formation> findByTypeAndActiveTrue(Enum.TrainingType type);
    // ===== FILTROS COMBINADOS =====

    @Query("""
        SELECT f
        FROM Formation f
        WHERE f.active = true
          AND f.minEducationLevel <= :educationLevel
          AND (f.minAcademicLevel IS NULL OR f.minAcademicLevel <= :academicLevel)
          AND (f.minAcademicXp IS NULL OR f.minAcademicXp <= :academicXp)
          AND (:careerInterest IS NULL 
               OR f.category = :careerInterest 
               OR :careerInterest MEMBER OF f.allowedCareers)
    """)
    List<Formation> findAvailableFormations(
            Enum.EducationLevel educationLevel,
            Integer academicLevel,
            Integer academicXp,
            Enum.CareerInterest careerInterest
    );

    // ===== BÚSQUEDA TEXTUAL (BONUS UX) =====

    @Query("""
        SELECT f
        FROM Formation f
        WHERE f.active = true
          AND (LOWER(f.name) LIKE LOWER(CONCAT('%', :text, '%'))
               OR LOWER(f.description) LIKE LOWER(CONCAT('%', :text, '%')))
    """)
    Page<Formation> searchActiveFormations(String text, Pageable pageable);
    List<Formation> findAllByActiveTrue();
}

