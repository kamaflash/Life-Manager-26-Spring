package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "job_vacancies")
@Data
public class JobVacancyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private JobPositionEntity position;

    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private Boolean active = true;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Integer availableSlots;

    @Enumerated(EnumType.STRING)
    private EnumAll.ContractType contractType;

    @Enumerated(EnumType.STRING)
    private EnumAll.WorkModality workModality;

    private Boolean visaSponsorship;
    private Integer weeklyHours;
    private LocalTime startTime;  // Hora de inicio (ej: 09:00:00)
    private LocalTime endTime;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<EnumAll.WorkingDay> workingDays;
    @Column(length = 2000)
    private String description;
    @ElementCollection
    private List<String> benefits;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequirementEntity> requirements;
}

