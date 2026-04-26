// PayrollSearchFiltersDTO.java
package com.pet.businessdomain.shareddto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollSearchFiltersDTO {

    // Paginación y ordenamiento
    private int page = 0;
    private int size = 10;
    private String sortBy = "periodStart";
    private String sortDir = "desc";

    // Filtros obligatorios
    private Long characterId;
    private Long jobId;

    // Filtro por período (formato: "2024-09" o "2024-09-01")
    private String period;

    // Búsqueda global (opcional)
    private String searchTerm;
}