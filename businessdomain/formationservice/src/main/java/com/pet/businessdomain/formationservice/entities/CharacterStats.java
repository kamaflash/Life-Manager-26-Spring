package com.pet.businessdomain.formationservice.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CharacterStats {

    private Integer intelligence;
    private Integer charisma;
    private Integer creativity;
    private Integer resilience;
    private Integer health;
    private Integer energy;
    private Integer happiness;
    private Integer stress;
    private Integer finances;
}

