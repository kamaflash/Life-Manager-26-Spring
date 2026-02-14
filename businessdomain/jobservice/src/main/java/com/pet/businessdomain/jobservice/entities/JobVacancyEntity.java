package com.pet.businessdomain.jobservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "job_vacancies")
@Data
public class JobVacancyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Salario
    private BigDecimal salary;         // Salario base
    private Integer payNumber;         // Número de pagos al año (12 mensual, 1 anual, etc.)

    // Estado de la vacante
    private Boolean active = true;     // Si la vacante sigue abierta
    private LocalDate openingDate;     // Fecha de apertura
    private LocalDate closingDate;     // Fecha de cierre
    private Integer availableSlots;    // Número de posiciones disponibles

    // Otros detalles de la oferta
    private String location;           // Ciudad o remoto
    private String contractType;       // FULL_TIME, PART_TIME, INTERNSHIP, FREELANCE
    private String perks;              // Beneficios adicionales (bonos, seguro, tickets comida)
    private Boolean remoteFriendly;    // Si la vacante permite teletrabajo
    private Boolean visaSponsorship;   // Si ofrece soporte de visa

    // Relación con el puesto
    @ManyToOne
    @JoinColumn(name = "position_id")
    private JobPositionEntity position;
}
