package com.pet.businessdomain.shareddto.dto;


import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import lombok.Data;
import java.util.List;

@Data
public class CompanyDTO {
    private Long id;
    private String name;
    private JobCategory category;
    private String description;
    private String location;
    private String website;
    private String logoUrl;
    private String contactEmail;
    private String phone;
    private Boolean active;
    private Integer employeesCount;
    private Integer foundedYear;
    private String size; // STARTUP, SME, CORPORATION, MULTINATIONAL
    private Integer reputation;
    private Boolean remoteFriendly;
    private Boolean internshipAvailable;
    private List<JobPositionDTO> positions;
}
