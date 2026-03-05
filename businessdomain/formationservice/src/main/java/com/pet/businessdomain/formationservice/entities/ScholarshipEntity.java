package com.pet.businessdomain.formationservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "scholarships")
@Data
public class ScholarshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private Double amount;

    private String country;

    private Boolean active = true;

    private Integer minXpRequired;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
