package com.pet.businessdomain.shareddto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DayInfoDTO {
    private String dayName;
    private int dayOfWeek;

    @JsonProperty("isWeekend")  // 🔥 Esto fuerza el nombre en el JSON
    private boolean isWeekend;

    @JsonProperty("isVacation")
    private boolean isVacation;

    @JsonProperty("isHoliday")
    private boolean isHoliday;

    private String season;
    private String weather;
}
