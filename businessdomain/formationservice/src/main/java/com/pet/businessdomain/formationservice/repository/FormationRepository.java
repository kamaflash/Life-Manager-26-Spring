/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pet.businessdomain.formationservice.repository;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.formationservice.entities.Formation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FormationRepository extends JpaRepository<Formation, Long> {

    // ===== BÁSICOS =====

    Optional<Formation> findByCode(String code);
    Page<Formation> findByCode(Pageable pageable, String code);

    List<Formation> findByActiveTrue();
    boolean existsByCode(String code);
    Page<Formation> findByActiveTrue(Pageable pageable);

    // ===== FILTROS SIMPLES =====

    List<Formation> findByCategoryAndActiveTrue(EnumAll.CareerInterest category);

    List<Formation> findByDifficultyAndActiveTrue(EnumAll.DifficultyLevel difficulty);

    List<Formation> findByTypeAndActiveTrue(EnumAll.TrainingType type);
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
            EnumAll.EducationLevel educationLevel,
            Integer academicLevel,
            Integer academicXp,
            EnumAll.CareerInterest careerInterest
    );
    @Query("""
    SELECT f
    FROM Formation f
    WHERE f.active = true
      AND f.minEducationLevel <= :educationLevel
      AND (f.minAcademicLevel IS NULL OR f.minAcademicLevel <= :academicLevel)
      AND (f.minAcademicXp IS NULL OR f.minAcademicXp <= :academicXp)
      AND (
            :careerInterest IS NULL
            OR f.category = :careerInterest
            OR :careerInterest MEMBER OF f.allowedCareers
          )
""")
    Page<Formation> findAvailableFormations(
            @Param("educationLevel") EnumAll.EducationLevel educationLevel,
            @Param("academicLevel") Integer academicLevel,
            @Param("academicXp") Integer academicXp,
            @Param("careerInterest") EnumAll.CareerInterest careerInterest,
            Pageable pageable
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

