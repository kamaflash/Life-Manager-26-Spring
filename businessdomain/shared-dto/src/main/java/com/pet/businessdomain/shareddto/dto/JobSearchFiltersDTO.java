package com.pet.businessdomain.shareddto.dto;


import lombok.Data;
import java.util.List;

@Data
public class JobSearchFiltersDTO {
    private String keyword; // búsqueda por título o empresa
    private List<String> categories; // TECNOLOGY, BUSINESS, etc.
    private List<String> contractTypes; // FULL_TIME, PART_TIME, etc.
    private List<String> workModalities; // ONSITE, REMOTE, HYBRID
    private List<String> levels; // JUNIOR, SENIOR, etc.
    private String location;
    private Integer minSalary;
    private Integer maxSalary;
    private Boolean remoteFriendly;
    private Boolean visaSponsorship;
    private Integer page;
    private Integer size;
}
