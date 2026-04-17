package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CharacterEventRecordResponseDto {

    private Long id;
    private Long characterId;
    private Long eventId;

    private String status;

    private LocalDateTime registeredAt;
    private LocalDateTime attendedAt;

    private boolean rewardsClaimed;

    private String outcome;
    private String outcomeNote;
}
