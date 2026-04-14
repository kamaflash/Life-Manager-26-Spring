package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationFiltersDTO {

    // Paginación
    private int page = 0;
    private int size = 10;
    private String sortBy = "appliedAt";
    private String sortDir = "desc";

    // Filtros
    private Long characterId;
    private List<String> status;      // PENDING, REVIEWING, OFFERED, HIRED, REJECTED
    private List<String> stage;       // APPLICATION, CV_REVIEW, PHONE_SCREEN, etc.
    private Integer minMatchScore;
    private Integer maxMatchScore;
    private String search;            // Búsqueda global por posición o empresa
    private String fromDate;          // Fecha desde
    private String toDate;            // Fecha hasta
}
