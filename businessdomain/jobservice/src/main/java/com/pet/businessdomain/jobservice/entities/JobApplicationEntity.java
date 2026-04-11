package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Data
public class JobApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterId;

    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    private JobVacancyEntity vacancy;

    private LocalDateTime appliedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private EnumAll.ApplicationStatus status; // PENDING, REVIEWING, INTERVIEW_SCHEDULED, OFFERED, HIRED, REJECTED

    private Integer matchScore; // 0-100, qué tan bien cumple requisitos

    private LocalDateTime interviewDate;
    private String interviewNotes;

    // 🔥 NUEVO: Seguimiento del proceso
    @Enumerated(EnumType.STRING)
    private EnumAll.ApplicationStage stage; // APPLICATION, CV_REVIEW, PHONE_SCREEN, TECHNICAL, HR, OFFER, HIRED

    @Column(precision = 10, scale = 2)
    private BigDecimal offeredSalary;
}
