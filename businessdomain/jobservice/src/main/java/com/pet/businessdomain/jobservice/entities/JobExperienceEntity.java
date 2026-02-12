package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "job_experiences")
@Data
public class JobExperienceEntity {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private JobCategory category;

    @OneToOne
    @JoinColumn(name = "position_id")
    private JobPositionEntity jobPosition;

    private Integer minYears;
    private Integer minLevel;
    private Integer minXp;

    @ElementCollection
    @CollectionTable(
            name = "job_experience_required_skills",
            joinColumns = @JoinColumn(name = "job_experience_id")
    )
    @Column(name = "skill")
    private List<String> requiredSkills;

    @ElementCollection
    @CollectionTable(
            name = "job_experience_optional_skills",
            joinColumns = @JoinColumn(name = "job_experience_id")
    )
    @Column(name = "skill")
    private List<String> optionalSkills;
}
