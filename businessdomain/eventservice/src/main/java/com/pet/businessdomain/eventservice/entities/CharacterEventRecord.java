package com.pet.businessdomain.eventservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "character_event_records")
@Data
public class CharacterEventRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterId;
    private Long eventId;

    @Enumerated(EnumType.STRING)
    private EnumAll.EventParticipationStatus status;
    // INVITED, REGISTERED, ATTENDED, MISSED, SKIPPED

    private LocalDateTime registeredAt;
    private LocalDateTime attendedAt;

    private boolean rewardsClaimed;

    // Outcome del evento si tiene resultados variables
    @Enumerated(EnumType.STRING)
    private EnumAll.EventOutcome outcome;
    // SUCCESS, PARTIAL, FAILURE, NEUTRAL

    private String outcomeNote;       // Descripción narrativa del resultado
}

