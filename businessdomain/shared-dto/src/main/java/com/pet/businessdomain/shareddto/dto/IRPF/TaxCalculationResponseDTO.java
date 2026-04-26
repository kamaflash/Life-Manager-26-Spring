package com.pet.businessdomain.shareddto.dto.IRPF;
import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;
@Data
public class TaxCalculationResponseDTO {
    private Long characterId;
    private Integer taxYear;
    private BigDecimal totalGrossIncome;
    private BigDecimal totalDeductions;
    private List<DeductionBreakdownDTO> deductionBreakdown;
    private BigDecimal taxableBase;
    private BigDecimal calculatedTax;
    private List<TaxBracketDTO> taxBracketBreakdown;
    private BigDecimal totalWithheld;
    private BigDecimal result;
    private String resultType;
    private BigDecimal effectiveTaxRate; // Tasa efectiva de impuesto (%)
}
