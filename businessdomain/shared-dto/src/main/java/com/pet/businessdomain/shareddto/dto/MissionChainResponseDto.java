package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.util.List;

@Data
public class MissionChainResponseDto {

    private Long id;
    private String code;
    private String title;
    private String description;
    private String lore;

    private String category;

    private String iconUrl;

    private List<MissionResponseDto> missions;
}
