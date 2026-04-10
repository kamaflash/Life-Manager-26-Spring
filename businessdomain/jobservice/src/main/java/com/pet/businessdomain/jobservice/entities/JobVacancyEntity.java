package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    // 💰 Salario
    private BigDecimal minSalary;
    private BigDecimal maxSalary;

    // 📅 Estado
    private Boolean active = true;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Integer availableSlots;

    // 📍 Detalles del puesto
    private String location;

    @Enumerated(EnumType.STRING)
    private EnumAll.ContractType contractType; // FULL_TIME, PART_TIME, INTERNSHIP, FREELANCE

    @Enumerated(EnumType.STRING)
    private EnumAll.WorkModality workModality; // ONSITE, REMOTE, HYBRID

    private Boolean visaSponsorship;

    // ⏰ Horario semanal
    private Integer weeklyHours; // 20, 30, 40, etc.

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<EnumAll.WorkingDay> workingDays; // MONDAY, TUESDAY...

    // 🔥 NUEVO: Beneficios específicos de esta vacante
    @ElementCollection
    private List<String> benefits; // "bonus", "meal_vouchers", "transport"

    // 🔥 NUEVO: Requisitos específicos
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequirementEntity> requirements;
}

