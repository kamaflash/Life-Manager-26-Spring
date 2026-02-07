package com.pet.businessdomain.personservice.dto;

import lombok.Data;

@Data
public class ItemDto {

    private Long id;

    private String name;
    private Integer qty;
    private String description;
}
