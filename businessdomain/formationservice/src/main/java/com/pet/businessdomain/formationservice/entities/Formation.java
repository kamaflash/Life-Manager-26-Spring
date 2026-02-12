package com.pet.businessdomain.formationservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import com.pet.businessdomain.formationservice.entities.enumentities.Enum;
@Entity
@Data
@Table(name = "formations")
public class Formation {

    @Id
    private Long id;

    // ===== IDENTIDAD =====
    private String code;          // Ej: JAVA_BASIC_01
    private String name;

    @Column(length = 1000)
    private String description;

    // ===== CLASIFICACIÓN =====
    @Enumerated(EnumType.STRING)
    private Enum.CareerInterest category;
    // Tecnología, Negocios, Artes, Salud, etc.

    @Enumerated(EnumType.STRING)
    private Enum.TrainingType type;
    // COURSE, DEGREE, MASTER, WORKSHOP

    @Enumerated(EnumType.STRING)
    private Enum.DifficultyLevel difficulty;
    // BASIC, INTERMEDIATE, ADVANCED

    // ===== REQUISITOS EDUCATIVOS =====
    @Enumerated(EnumType.STRING)
    private Enum.EducationLevel minEducationLevel;  // Nivel educativo mínimo requerido

    private Integer minAcademicLevel; // Nivel mínimo de conocimiento previo
    private Integer minAcademicXp;    // Experiencia mínima requerida
    private Integer maxAcademicXp;    // Experiencia máxima permitida

    @ElementCollection
    private List<Enum.CareerInterest> allowedCareers; // Carreras permitidas

    // ===== COSTE =====
    private Integer durationHours;    // Duración en horas
    private BigDecimal cost;              // Coste monetario
    private Integer effort;           // Energía / estrés requerido

    // ===== RESULTADO / BENEFICIOS =====
    private Integer academicXpReward; // XP que otorga al completar

    @ElementCollection
    private List<String> skillsUnlocked; // Habilidades desbloqueadas (valores clave, ej: "basic_computer")

    private Boolean repeatable; // Si se puede repetir
    private Boolean active;     // Si la formación está activa

    // ===== TRACKING / ESTADÍSTICAS =====
    private Integer level = 1;      // Nivel de dificultad interno
    private Integer xp = 0;         // XP ganado por el usuario en esta formación
    private Boolean locked = false; // Si la formación está bloqueada
    private String lastPracticed;   // Fecha de última práctica
    private String decayRate;       // Ritmo de decaimiento del conocimiento
    @Embedded
    private CharacterStats stats;

}
