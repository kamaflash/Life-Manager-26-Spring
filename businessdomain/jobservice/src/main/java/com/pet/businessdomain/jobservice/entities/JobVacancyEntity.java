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

    // 💰 Salario
    private BigDecimal salary;
    private Integer payNumber;

    // 📅 Estado de la vacante
    private Boolean active = true;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Integer availableSlots;

    // 📍 Detalles
    private String location;
    private String contractType;
    private String perks;
    private Boolean remoteFriendly;
    private Boolean visaSponsorship;

    // ⏰ Horario
    private Integer weeklyHours; // máximo 40

    private LocalTime startTime;
    private LocalTime endTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "job_working_days", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private List<EnumAll.WorkingDay> workingDays = new ArrayList<>();

    // 🔗 Relación
    @ManyToOne
    @JoinColumn(name = "position_id")
    private JobPositionEntity position;
}
