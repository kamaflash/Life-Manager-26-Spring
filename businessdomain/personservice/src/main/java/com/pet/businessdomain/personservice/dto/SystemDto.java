package com.pet.businessdomain.personservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemDto {
    private Long id;
    private Long uid;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime actualityAt = LocalDateTime.now();
}
