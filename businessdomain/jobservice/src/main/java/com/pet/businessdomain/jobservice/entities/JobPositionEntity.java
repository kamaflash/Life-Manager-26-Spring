package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "job_positions")
@Data
public class JobPositionEntity {

    @Id
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private EnumAll.JobLevel level; // JUNIOR, SEMI_SENIOR, SENIOR, LEAD, MANAGER, DIRECTOR

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private JobCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    private Boolean active = true;

    // 🔥 NUEVO: Rama de especialización
    @Enumerated(EnumType.STRING)
    private EnumAll.CareerPath careerPath; // TECHNOLOGY, BUSINESS, HEALTH, CREATIVE, CONSTRUCTION, SOCIAL

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobVacancyEntity> vacancies;

    // 🔥 NUEVO: Perks que desbloquea este puesto
    @ElementCollection
    private List<String> perks; // "health_insurance", "gym_membership", "stock_options"
}

