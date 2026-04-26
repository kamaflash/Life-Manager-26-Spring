package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.util.List;

@Data
public class BulkTaxFilingResultDTO {
    private Integer totalProcessed;
    private Integer successful;
    private Integer failed;
    private List<BulkTaxFilingErrorDTO> errors;
    private StatisticsDTO statistics;
}
