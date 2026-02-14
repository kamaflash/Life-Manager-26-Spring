package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

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

    private String location;           // Ciudad o sede principal
    private String website;            // URL
    private String logoUrl;            // Imagen de la empresa
    private String contactEmail;       // Correo de RRHH
    private String phone;              // Teléfono de contacto
    private boolean active = true;

    private Integer employeesCount;    // Tamaño de la empresa
    private Integer foundedYear;       // Año de fundación

    private Boolean remoteFriendly;    // Si acepta teletrabajo
    private Boolean internshipAvailable; // Si tiene prácticas

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobPositionEntity> positions;

}