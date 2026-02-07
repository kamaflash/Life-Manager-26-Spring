package com.pet.businessdomain.personservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class SkillXpEmbeddable {
    @Column(name = "skill_key")
    private String key;
    private int xp;
}
