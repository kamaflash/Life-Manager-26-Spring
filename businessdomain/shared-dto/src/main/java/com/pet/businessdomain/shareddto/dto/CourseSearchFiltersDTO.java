package com.pet.businessdomain.shareddto.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CourseSearchFiltersDTO {
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "name";
    private String sortDir = "asc";
    private String searchTerm;
    private String category;
    private String level;
    private Integer minDuration;
    private Integer maxDuration;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
