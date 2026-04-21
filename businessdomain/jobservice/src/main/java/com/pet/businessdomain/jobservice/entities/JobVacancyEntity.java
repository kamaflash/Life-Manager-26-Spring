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
    private LocalTime startTime;
    private LocalTime endTime;

    @ElementCollection
    @CollectionTable(
            name = "job_vacancies_working_days",  // 🔥 Nombre exacto de la tabla
            joinColumns = @JoinColumn(name = "job_vacancy_id")  // 🔥 Nombre de la columna FK
    )
    @Column(name = "working_days")  // 🔥 Nombre de la columna en la tabla
    @Enumerated(EnumType.STRING)
    private List<EnumAll.WorkingDay> workingDays;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    private List<String> benefits;

    // CORREGIDO: Añadir mappedBy y cascade
    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RequirementEntity> requirements = new ArrayList<>();

    // Método helper para mantener consistencia
    public void addRequirement(RequirementEntity requirement) {
        requirements.add(requirement);
        requirement.setVacancy(this);
    }

    public void removeRequirement(RequirementEntity requirement) {
        requirements.remove(requirement);
        requirement.setVacancy(null);
    }
}