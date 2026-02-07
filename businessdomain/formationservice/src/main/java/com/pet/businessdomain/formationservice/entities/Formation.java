/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.formationservice.entities;

import com.pet.businessdomain.formationservice.entities.enumentities.Enum;
import jakarta.persistence.*;

import java.util.List;

import lombok.Data;

/**
 *
 * @author Pc
 */
@Entity
@Data
@Table(name = "formations")
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== IDENTIDAD =====
    private String code;              // ej: JAVA_BASIC_01
    private String name;

    @Column(length = 1000)
    private String description;

    // ===== CLASIFICACIÓN =====
    @Enumerated(EnumType.STRING)
    private Enum.CareerInterest category;
    // TECHNOLOGY, BUSINESS, ARTS, HEALTH...

    @Enumerated(EnumType.STRING)
    private Enum.TrainingType type;
    // COURSE, DEGREE, MASTER, WORKSHOP

    @Enumerated(EnumType.STRING)
    private Enum.DifficultyLevel difficulty;
    // BASIC, INTERMEDIATE, ADVANCED

    // ===== REQUISITOS =====
    @Enumerated(EnumType.STRING)
    private Enum.EducationLevel minEducationLevel;

    private Integer minAcademicLevel;
    private Integer minAcademicXp;
    private Integer maxAcademicXp;

    @ElementCollection
    private List<Enum.CareerInterest> allowedCareers;

    // ===== COSTE =====
    private Integer durationHours;
    private Double cost;
    private Integer effort; // energía / estrés requerido

    // ===== RESULTADO =====
    private Integer academicXpReward;

    @ElementCollection
    private List<String> skillsUnlocked;

    private Boolean repeatable;
    private Boolean active;

}