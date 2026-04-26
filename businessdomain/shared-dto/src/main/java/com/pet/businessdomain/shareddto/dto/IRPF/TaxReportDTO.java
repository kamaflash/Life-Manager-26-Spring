package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.util.Map;

@Data
public class TaxReportDTO {
    private Integer taxYear;
    private Long totalFilings;
    private Long submittedFilings;
    private Long pendingFilings;
    private TaxReportSummaryDTO summary;
    private Map<Integer, TaxReportBracketDTO> bracketsDistribution;
}
