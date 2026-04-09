package com.pet.businessdomain.shareddto.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SystemDto {
    private Long id;
    private Long uid;
    private LocalDateTime createdAt = LocalDate.now()
            .withMonth(9)
            .withDayOfMonth(10)
            .atStartOfDay();
    private LocalDateTime updateAt = LocalDate.now()
            .withMonth(9)
            .withDayOfMonth(10)
            .atStartOfDay();
    private LocalDateTime actualityAt = LocalDate.now()
            .withMonth(9)
            .withDayOfMonth(10)
            .atStartOfDay();
    private Integer veces = 0;
    private Integer pa;
}
