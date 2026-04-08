package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class StatsDto {

    private Integer intelligence;
    private Integer charisma;
    private Integer creativity;
    private Integer resilience;

    private Integer health;
    private Integer energy;
    private Integer happiness;
    private Integer stress;

    private Integer finances;
}
