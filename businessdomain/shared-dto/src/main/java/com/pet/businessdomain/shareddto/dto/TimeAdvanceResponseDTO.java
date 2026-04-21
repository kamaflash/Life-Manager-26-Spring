package com.pet.businessdomain.shareddto.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class TimeAdvanceResponseDTO {
    private boolean success;
    private String message;
    private LocalDateTime newActualityAt;
    private int paRemaining;
    private int energyChange;
    private int stressChange;
    private Map<String, Integer> statChanges;
    private List<DayEventDTO> events;
    private DayInfoDTO dayInfo;
}
