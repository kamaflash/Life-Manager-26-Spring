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

    @Column(columnDefinition = "VARCHAR(255)")
    private String title;

    @Enumerated(EnumType.STRING)
    private EnumAll.JobLevel level;

    @Column(length = 1000, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private JobCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    private EnumAll.CareerPath careerPath;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobVacancyEntity> vacancies;

    @ElementCollection
    private List<String> perks;
}