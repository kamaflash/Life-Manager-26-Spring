package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumSystems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor  // ← Agrega esto
@AllArgsConstructor
public class TimeAdvanceRequestDTO {
    private Long characterId;
    private EnumSystems.AdvanceType advanceType;
    private LocalDateTime targetTime;
    private Boolean forceAdvance;
}
