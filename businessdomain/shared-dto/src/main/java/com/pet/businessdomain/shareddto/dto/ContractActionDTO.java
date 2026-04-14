package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractActionDTO {
    private Long contractId;
    private boolean accepted; // true = aceptar, false = rechazar
    private String notes; // opcional: notas adicionales
}