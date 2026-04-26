package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TaxBracketDTO {
    private BigDecimal minimum;
    private BigDecimal maximum;
    private BigDecimal rate;
    private BigDecimal taxableAmount; // Cantidad que aplica a este tramo
    private BigDecimal taxAmount; // Impuesto generado en este tramo
}
