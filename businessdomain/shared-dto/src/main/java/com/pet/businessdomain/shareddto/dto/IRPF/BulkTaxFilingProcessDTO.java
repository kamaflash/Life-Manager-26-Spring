package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;
import java.util.List;

@Data
public class BulkTaxFilingProcessDTO {
    private Integer taxYear;
    private List<Long> characterIds; // Si null, procesar todos
    private Boolean simulateOnly; // Solo simular, no guardar
    private Boolean sendNotifications;
}
