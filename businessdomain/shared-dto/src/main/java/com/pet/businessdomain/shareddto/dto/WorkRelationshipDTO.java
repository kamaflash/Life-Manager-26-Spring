package com.pet.businessdomain.shareddto.dto;


import lombok.Data;

@Data
public class WorkRelationshipDTO {
    private Long id;
    private Long relatedCharacterId;
    private String relatedCharacterName;
    private String relationshipType; // BOSS, MENTOR, COLLEAGUE, RIVAL
    private Integer affinity; // -100 a 100
    private String notes;
}
