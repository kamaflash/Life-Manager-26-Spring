package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
public class CompanyEntity {

    @Id
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)")
    private String name;

    @Enumerated(EnumType.STRING)
    private JobCategory category;

    @Column(length = 1000, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "VARCHAR(255)")
    private String location;

    private String website;
    private String logoUrl;
    private String contactEmail;
    private String phone;
    private boolean active = true;
    private Integer employeesCount;
    private Integer foundedYear;
    private Boolean remoteFriendly;
    private Boolean internshipAvailable;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobPositionEntity> positions;
}