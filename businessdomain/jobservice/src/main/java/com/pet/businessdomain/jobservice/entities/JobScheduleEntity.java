package com.pet.businessdomain.jobservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "job_schedules")
@Data
public class JobScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Día de la semana (MONDAY, TUESDAY...)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    // Hora inicio y fin
    private LocalTime startTime;
    private LocalTime endTime;

    // Relación con vacante
    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    private JobVacancyEntity vacancy;
}
