package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class UpdateObjectiveProgressRequestDto {

    private Long characterId;
    private Long missionId;
    private Long objectiveId;
    private Integer value; // incremento o valor absoluto según tu lógica
}
