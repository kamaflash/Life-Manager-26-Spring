package com.pet.businessdomain.jobservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "character_applications")
@Data
public class CharacterApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Solo el ID del personaje
    @Column(name = "character_id", nullable = false)
    private Long characterId;

    // Solo el ID de la vacante
    @Column(name = "vacancy_id", nullable = false)
    private Long vacancyId;

    private LocalDateTime appliedAt = LocalDateTime.now();
    private String status; // APPLIED, INTERVIEW, HIRED, REJECTED

    @ElementCollection
    private List<String> matchedSkills; // Habilidades del personaje que coinciden con el puesto

    private Integer matchScore; // Puntaje de coincidencia según habilidades y experiencia
}
