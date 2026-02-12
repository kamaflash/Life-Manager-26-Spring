package com.pet.businessdomain.formationservice.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CharacterStats {

    private int intelligence;
    private int charisma;
    private int creativity;
    private int resilience;
    private int health;
    private int energy;
    private int happiness;
    private int stress;
    private int finances;
}

