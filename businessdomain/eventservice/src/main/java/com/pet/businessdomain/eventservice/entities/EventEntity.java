package com.pet.businessdomain.eventservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;               // "EVENT_TECH_FAIR_2024"
    private String title;
    private String description;
    private String lore;

    @Enumerated(EnumType.STRING)
    private EnumAll.EventType type;
    // SOCIAL, ACADEMIC, PROFESSIONAL, CULTURAL,
    // SEASONAL, RANDOM, CRISIS, OPPORTUNITY

    @Enumerated(EnumType.STRING)
    private EnumAll.EventScope scope;
    // GLOBAL (todos los personajes), CITY, PERSONAL

    private String city;               // null si es GLOBAL

    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime endDate = LocalDateTime.now();

    private boolean recurring;
    private String cronExpression;     // Para eventos recurrentes: "0 0 0 1 1 ?" (Año Nuevo)

    private Integer maxParticipants;   // null = ilimitado
    private Integer durationMinutes;   // Duración del evento en tiempo de juego

    @Enumerated(EnumType.STRING)
    private EnumAll.EventStatus status;        // SCHEDULED, ACTIVE, FINISHED, CANCELLED

    private boolean autoTrigger;       // Se activa solo sin que el jugador lo acepte

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<RewardEntity> rewards;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<RequirementEntity> requirements;

    private String iconUrl;
    private String bannerUrl;

    private LocalDateTime createdAt = LocalDateTime.now();
}

