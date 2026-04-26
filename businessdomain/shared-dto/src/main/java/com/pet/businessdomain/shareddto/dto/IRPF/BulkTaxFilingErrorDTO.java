package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

@Data
public class BulkTaxFilingErrorDTO {
    private Long characterId;
    private String errorMessage;
    private String timestamp;
}
