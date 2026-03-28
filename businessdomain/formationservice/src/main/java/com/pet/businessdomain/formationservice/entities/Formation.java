package com.pet.businessdomain.formationservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
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
    private EnumAll.CareerInterest category;
    // Tecnología, Negocios, Artes, Salud, etc.

    @Enumerated(EnumType.STRING)
    private EnumAll.TrainingType type;
    // COURSE, DEGREE, MASTER, WORKSHOP

    @Enumerated(EnumType.STRING)
    private EnumAll.DifficultyLevel difficulty;
    // BASIC, INTERMEDIATE, ADVANCED

    // ===== REQUISITOS EDUCATIVOS =====
    @Enumerated(EnumType.STRING)
    private EnumAll.EducationLevel minEducationLevel;  // Nivel educativo mínimo requerido

    private Integer minAcademicLevel; // Nivel mínimo de conocimiento previo
    private Integer minAcademicXp;    // Experiencia mínima requerida
    private Integer maxAcademicXp;    // Experiencia máxima permitida

    @ElementCollection
    private List<EnumAll.CareerInterest> allowedCareers; // Carreras permitidas

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

    private LocalTime startTime;
    private LocalTime endTime;

    @ElementCollection
    @CollectionTable(name = "course_working_days", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private List<EnumAll.WorkingDay> workingDays;
}
