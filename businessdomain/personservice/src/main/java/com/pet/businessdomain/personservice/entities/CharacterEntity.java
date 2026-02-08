package com.pet.businessdomain.personservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pet.businessdomain.personservice.dto.SFinanceAccountResponseDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "characters")
@Data
public class CharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long uid;

    private String name;
    private Integer age;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDate;

    private String gender;
    private String city;
    private String residentialCity;
    private Integer xpAcademy;
    private Integer xpJobs;

    @Embedded
    private CharacterStats stats;

    // ===== SKILLS =====
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "character_skills",
            joinColumns = @JoinColumn(name = "character_id")
    )
    @MapKeyColumn(name = "skill_key")
    @AttributeOverrides({
            @AttributeOverride(name = "key", column = @Column(name = "skill_key_value"))
    })
    private Map<String, SkillStateEmbeddable> skills;

    // ===== EXPERIENCIAS =====
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    private List<EducationExperienceEntity> education;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    private List<JobExperienceEntity> jobs;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    private List<SocialExperienceEntity> social;

    // ===== RELACIONES =====
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    private List<RelationshipEntity> relationships;

    // ===== INVENTARIO =====
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    private List<ItemEntity> inventory;

    // ===== RELACIONES SIMPLES / IDS =====
    @ElementCollection
    @CollectionTable(name = "character_friends", joinColumns = @JoinColumn(name = "character_id"))
    @Column(name = "friend_id")
    private List<Long> friends;

    private Long partnerId;

    @ElementCollection
    @CollectionTable(name = "character_family", joinColumns = @JoinColumn(name = "character_id"))
    @Column(name = "family_id")
    private List<Long> familyIds;

    @ElementCollection
    @CollectionTable(name = "character_interests", joinColumns = @JoinColumn(name = "character_id"))
    @Column(name = "interest")
    private List<String> interests;

    // ===== METADATOS =====
    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Transient
    private List<SFinanceAccountResponseDto> accounts;
}
