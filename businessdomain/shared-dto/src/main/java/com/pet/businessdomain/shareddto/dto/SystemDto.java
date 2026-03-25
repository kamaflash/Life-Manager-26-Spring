package com.pet.businessdomain.shareddto.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SystemDto {
    private Long id;
    private Long uid;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updateAt = LocalDateTime.now();
    private LocalDateTime actualityAt = LocalDateTime.now();
    private Integer veces;
}
