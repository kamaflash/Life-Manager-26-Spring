package com.pet.businessdomain.eventservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "requirements")
@Data
public class RequirementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EnumAll.RequirementType type;
    // MIN_AGE, MIN_STAT, MIN_XP_JOBS, MIN_XP_ACADEMY,
    // MISSION_COMPLETED, EVENT_ATTENDED, HAS_ITEM,
    // HAS_PARTNER, HAS_FRIENDS_COUNT, MIN_MONEY, HAS_TITLE

    private String targetKey;      // stat/skill key, mission code, item code...
    private Integer targetValue;
    private String stringValue;    // Para valores string (título requerido, ciudad, etc.)

    private String failMessage;    // Mensaje si no se cumple el requisito
}

