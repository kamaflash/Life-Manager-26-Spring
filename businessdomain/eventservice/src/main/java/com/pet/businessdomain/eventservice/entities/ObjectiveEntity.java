package com.pet.businessdomain.eventservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mission_objectives")
@Data
public class ObjectiveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;        // "Trabaja 5 días seguidos"

    @Enumerated(EnumType.STRING)
    private EnumAll.ObjectiveType type;
    // REACH_STAT, REACH_XP, COMPLETE_ACTIONS, BUY_ITEM,
    // MAKE_FRIEND, GET_PARTNER, REACH_AGE, EARN_MONEY,
    // ATTEND_EVENT, COMPLETE_MISSION

    private String targetKey;          // stat/skill key cuando aplica: "intelligence", "charisma"
    private Integer targetValue;       // valor objetivo
    private Integer currentDefault;    // valor de inicio (normalmente 0)

    private boolean optional;          // Si es un objetivo secundario
    private Integer orderIndex;        // Orden de presentación

    private String hint;               // Pista para el jugador
}

