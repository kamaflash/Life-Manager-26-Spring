package com.pet.businessdomain.jobservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "character_jobs")
@Data
public class CharacterJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long characterId;

    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    private JobVacancyEntity vacancy;

    private LocalDate startDate;
    private LocalDate endDate; // null si sigue activo
    private Boolean active = true;

    // 🔥 NUEVO: Progreso en el puesto
    private Integer performance; // 0-100 rendimiento
    private Integer satisfaction; // 0-100 satisfacción laboral
    private Integer stressLevel; // 0-100 estrés acumulado

    // 🔥 NUEVO: Historial
    private Integer promotionsReceived = 0;
    private Integer bonusesReceived = 0;
    private LocalDateTime lastPromotionDate;

    // 🔥 NUEVO: Relaciones laborales
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkRelationshipEntity> relationships;
}
