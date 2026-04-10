package com.pet.businessdomain.shareddto.dto;


import lombok.Data;

@Data
public class WorkProgressDTO {
    private Long characterJobId;
    private Integer hoursWorked;
    private Integer productivityGain;
    private Integer stressGain;
    private Integer satisfactionChange;
    private Integer salaryEarned;
    private Boolean eventTriggered;
    private JobEventDTO triggeredEvent;
}
