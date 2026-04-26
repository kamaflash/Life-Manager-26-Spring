package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TaxCalculationRequestDTO {
    private Long characterId;
    private Integer taxYear;
    private BigDecimal additionalDeductions; // Deducciones adicionales no estándar
    private List<Long> dependents; // IDs de dependientes (hijos, etc.)
    private Boolean jointFiling; // Declaración conjunta con cónyuge
    private Long spouseId; // ID del cónyuge si jointFiling = true
}
