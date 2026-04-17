package com.pet.businessdomain.eventservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "missions")
@Data
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;           // Identificador único legible: "MISSION_FIRST_JOB"
    private String title;
    private String description;
    private String lore;           // Texto narrativo/sabor RPG

    @Enumerated(EnumType.STRING)
    private EnumAll.MissionType type;      // MAIN, SIDE, DAILY, CHAIN, HIDDEN

    @Enumerated(EnumType.STRING)
    private EnumAll.MissionCategory category; // CAREER, SOCIAL, ACADEMIC, FINANCIAL, PERSONAL

    @Enumerated(EnumType.STRING)
    private EnumAll.MissionDifficulty difficulty; // EASY, MEDIUM, HARD, LEGENDARY

    private Integer minAge;           // Edad mínima del personaje para desbloquear
    private Integer requiredXpJobs;
    private Integer requiredXpAcademy;

    private boolean repeatable;
    private Integer cooldownHours;    // Si es repeatable, horas hasta que vuelve a estar disponible

    private boolean hidden;           // Oculta hasta cumplir requisitos
    private boolean autoAccept;       // Se acepta automáticamente al cumplir requisitos

    private Integer orderInChain;     // Posición si pertenece a una cadena
    private Long chainId;             // FK a MissionChainEntity

    private String iconUrl;
    private String bannerUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mission_id")
    private List<ObjectiveEntity> objectives;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mission_id")
    private List<RewardEntity> rewards;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mission_id")
    private List<RequirementEntity> requirements;

    @ElementCollection
    @CollectionTable(name = "mission_unlock_codes", joinColumns = @JoinColumn(name = "mission_id"))
    @Column(name = "unlocks_mission_code")
    private List<String> unlocksMissionCodes; // Misiones que se desbloquean al completar esta

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
