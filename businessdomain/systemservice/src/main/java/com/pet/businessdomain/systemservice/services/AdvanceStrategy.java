package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.TimeAdvanceRequestDTO;
import com.pet.businessdomain.shareddto.dto.TimeAdvanceResponseDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumSystems;

public interface AdvanceStrategy {
    TimeAdvanceResponseDTO execute(TimeAdvanceRequestDTO request);
    EnumSystems.AdvanceType getType();
}
