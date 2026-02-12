package com.pet.businessdomain.jobservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;

@Entity
@Table(name = "companies")
@Data
public class CompanyEntity {

    @Id
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private JobCategory category;

    @Column(length = 1000)
    private String description;

    private String location;
    private String website;
    private boolean active = true;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobPositionEntity> positions;
}
