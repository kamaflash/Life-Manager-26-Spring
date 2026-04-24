package com.pet.businessdomain.shareddto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DayInfoDTO {
    private String dayName;
    private int dayOfWeek;

    @JsonProperty("isWeekend")
    private boolean isWeekend;

    @JsonProperty("isVacation")
    private boolean isVacation;

    @JsonProperty("isHoliday")
    private boolean isHoliday;

    private String season;
    private String weather;

    // ✨ NUEVOS CAMPOS PARA FIN DE MES
    private Integer currentDay;
    private Integer currentMonth;
    private Integer currentYear;
    private Integer daysInMonth;

    @JsonProperty("isLastDayOfMonth")
    private boolean isLastDayOfMonth;

    @JsonProperty("isLastWeekOfMonth")
    private boolean isLastWeekOfMonth;

    @JsonProperty("isLastThreeDays")
    private boolean isLastThreeDays;

    private Integer daysUntilEndOfMonth;
    private Double monthProgress;

    // Opcional: días de pago o eventos especiales
    @JsonProperty("isPayDay")
    private boolean isPayDay;

    @JsonProperty("isMonthEndEvent")
    private boolean isMonthEndEvent;
}