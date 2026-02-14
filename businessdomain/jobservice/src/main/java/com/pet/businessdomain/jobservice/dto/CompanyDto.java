package com.pet.businessdomain.jobservice.dto;

import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
import lombok.Data;
import java.util.List;

@Data
public class CompanyDto {
    private Long id;
    private String name;
    private JobCategory category;   // technology, health, construction, business, creative, social, science, hospitality
    private String description;
    private String location;
    private String website;            // URL
    private String logoUrl;            // Imagen de la empresa
    private String contactEmail;       // Correo de RRHH
    private String phone;
    private boolean active;

    private Integer employeesCount;    // Tamaño de la empresa
    private Integer foundedYear;       // Año de fundación

    private Boolean remoteFriendly;    // Si acepta teletrabajo
    private Boolean internshipAvailable;
    private List<JobPositionDto> positions;  // Opcional, se puede cargar solo si se desea
}
