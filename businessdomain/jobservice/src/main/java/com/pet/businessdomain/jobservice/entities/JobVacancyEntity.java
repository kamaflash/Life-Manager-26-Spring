package com.pet.businessdomain.jobservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "job_vacancies")
@Data
public class JobVacancyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal salary;
    private Integer payNumber;
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private JobPositionEntity position;
}
