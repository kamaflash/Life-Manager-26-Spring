package com.pet.businessdomain.jobservice.entities;


import jakarta.persistence.*;
import lombok.Data;

// 🔥 NUEVO: Relaciones laborales
@Entity
@Table(name = "work_relationships")
@Data
public class WorkRelationshipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterJobId;
    private Long relatedCharacterId; // compañero/jefe
    private String relationshipType; // BOSS, MENTOR, COLLEAGUE, RIVAL
    private Integer affinity; // -100 a 100
}
