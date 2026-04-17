package com.pet.businessdomain.eventservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rewards")
@Data
public class RewardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EnumAll.RewardType type;
    // XP_JOBS, XP_ACADEMY, STAT_BOOST, SKILL_BOOST,
    // MONEY, ITEM, TITLE, UNLOCK_MISSION, UNLOCK_EVENT,
    // RELATIONSHIP_BONUS, BADGE

    private String targetKey;      // stat key, skill key, item code, etc.
    private Integer value;         // cantidad de XP, dinero, puntos de stat, etc.
    private String itemCode;       // si el reward es un ítem
    private String titleGranted;   // si el reward es un título ("Primer Empleo", "Graduado")
    private String badgeCode;      // código del badge/logro

    private boolean isPrimary;     // Recompensa principal vs. bonus
}

