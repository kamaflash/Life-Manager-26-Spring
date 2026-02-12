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
    private String website;
    private boolean active;
    private List<JobPositionDto> positions;  // Opcional, se puede cargar solo si se desea
}
