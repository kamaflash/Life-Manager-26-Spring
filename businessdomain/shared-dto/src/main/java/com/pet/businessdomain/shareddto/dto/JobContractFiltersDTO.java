package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobContractFiltersDTO {

    // Paginación
    private int page = 0;
    private int size = 10;
    private String sortBy = "issuedAt";
    private String sortDir = "desc";

    // Filtros
    private Long characterId;
    private List<String> status;        // PENDING, ACCEPTED, REJECTED, SIGNED
    private String search;              // Búsqueda global por posición o empresa
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String fromDate;            // Fecha desde (issuedAt)
    private String toDate;              // Fecha hasta (issuedAt)
    private String contractType;        // FULL_TIME, PART_TIME, INTERNSHIP
    private String workModality;        // ONSITE, REMOTE, HYBRID
}