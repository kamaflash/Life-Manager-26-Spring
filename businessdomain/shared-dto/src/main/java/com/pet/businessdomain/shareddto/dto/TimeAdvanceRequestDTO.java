package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumSystems;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TimeAdvanceRequestDTO {
    private Long characterId;
    private EnumSystems.AdvanceType advanceType;
    private LocalDateTime targetTime;
    private Boolean forceAdvance;
}
