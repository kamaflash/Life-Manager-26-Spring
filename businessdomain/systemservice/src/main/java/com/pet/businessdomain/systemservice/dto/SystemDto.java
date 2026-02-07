package com.pet.businessdomain.systemservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.pet.businessdomain.systemservice.entities.enumentities.Enum;
import lombok.Data;

@Data
public class SystemDto {
    private Long id;
    private Long uid;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime actualityAt = LocalDateTime.now();
}
