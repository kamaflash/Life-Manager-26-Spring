package com.pet.businessdomain.formationservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "scholarshipapplication")
@NoArgsConstructor      // 🔹 necesario para Hibernate
@AllArgsConstructor     // 🔹 necesario para @Builder
@Data
@Builder
public class ScholarshipApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // La beca a la que se aplica
    @JoinColumn(name = "scholarship_id", nullable = false)
    private Long scholarshipId;

    // Id del personaje que aplica
    @Column(name = "character_id", nullable = false)
    private Long characterId;

    // Estado de la solicitud
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;
    private LocalDate appliedAt;

    public enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
