package com.pet.businessdomain.formationservice.repository;

import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FormationRepository extends JpaRepository<Formation, Long> {

    List<Formation> findByActiveTrue();
    Page<Formation> findByActiveTrue(Pageable pageable);

    Optional<Formation> findByCode(String code);
    boolean existsByCode(String code);

    // Filtrar por categoría
    List<Formation> findByCategory(EnumAll.CareerInterest category);
    Page<Formation> findByCategory(EnumAll.CareerInterest category, Pageable pageable);

    @Query("SELECT f FROM Formation f WHERE f.active = true " +
            "AND f.minEducationLevel <= :educationLevel " +
            "AND f.minAcademicLevel <= :academicLevel " +
            "AND f.minAcademicXp <= :academicXp " +
            "AND (:careerInterest IS NULL OR :careerInterest MEMBER OF f.allowedCareers)")
    Page<Formation> findAvailableFormations(
            @Param("educationLevel") EnumAll.EducationLevel educationLevel,
            @Param("academicLevel") Integer academicLevel,
            @Param("academicXp") Integer academicXp,
            @Param("careerInterest") EnumAll.CareerInterest careerInterest,
            Pageable pageable
    );
}