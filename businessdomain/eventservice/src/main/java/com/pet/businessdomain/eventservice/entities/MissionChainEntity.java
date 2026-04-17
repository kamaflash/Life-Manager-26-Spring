package com.pet.businessdomain.eventservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mission_chains")
@Data
public class MissionChainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;           // "CHAIN_ENTREPRENEUR_PATH"
    private String title;
    private String description;
    private String lore;

    @Enumerated(EnumType.STRING)
    private EnumAll.MissionCategory category;

    private String iconUrl;

    @OneToMany(mappedBy = "chainId")
    private List<MissionEntity> missions;

    private LocalDateTime createdAt = LocalDateTime.now();
}

