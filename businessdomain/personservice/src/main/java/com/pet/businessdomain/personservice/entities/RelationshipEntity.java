package com.pet.businessdomain.personservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "relationships")
@Data
public class RelationshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String personId;
    private String type;
    private int closeness;
    private int trust;
    private Integer interactionCount;
}

