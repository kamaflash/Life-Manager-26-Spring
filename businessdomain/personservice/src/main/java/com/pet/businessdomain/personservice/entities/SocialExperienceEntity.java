package com.pet.businessdomain.personservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "social_experience")
@Data
public class SocialExperienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tipo de evento (fiesta, cita, reunión, etc.)
    private String eventType;

    private String date;

    private Integer durationHours;
    private Integer participants;

    // Resultado subjetivo
    private Integer socialScore;

    // Skills sociales ganadas
    @ElementCollection
    @CollectionTable(
            name = "social_skills_gained",
            joinColumns = @JoinColumn(name = "social_id")
    )
    private List<SkillXpEmbeddable> skillsGained;
}

