package com.pet.businessdomain.personservice.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class SkillStateEmbeddable {
    private String label;        // Clave de la habilidad
    private String keyValue;        // Clave de la habilidad
    private int level;         // Nivel
    private int xp;            // Experiencia
    private boolean locked;    // Bloqueada o no

    private Double decayRate;       // Opcional
    private String lastPracticed;   // Opcional, ISO String
}

